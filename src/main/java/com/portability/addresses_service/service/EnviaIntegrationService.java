package com.portability.addresses_service.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portability.addresses_service.config.EnviaConfig;
import com.portability.addresses_service.dto.CarriersOptionsRequest;
import com.portability.addresses_service.dto.CreateLabelRequest;
import com.portability.addresses_service.dto.EnviaApiCarriersOptionsRequest;
import com.portability.addresses_service.dto.EnviaApiCreateLabelRequest;
import com.portability.addresses_service.dto.EnviaCancelRequest;
import com.portability.addresses_service.dto.EnviaCarriersOptionsShipment;
import com.portability.addresses_service.dto.EnviaCreateLabelShipment;
import com.portability.addresses_service.dto.EnviaDeliveryDate;
import com.portability.addresses_service.dto.EnviaDimensions;
import com.portability.addresses_service.dto.EnviaPackages;
import com.portability.addresses_service.dto.EnviaQuoteResponse;
import com.portability.addresses_service.dto.EnviaSettings;
import com.portability.addresses_service.dto.EnviaTrackingRequest;
import com.portability.addresses_service.dto.EnviaTrackingResponse;
import com.portability.addresses_service.dto.ZipCodeRequest;
import com.portability.addresses_service.dto.ZipCodeResponse;
import com.portability.addresses_service.exception.InvalidAddressException;
import com.portability.addresses_service.exception.NotEnoughMoneyException;
import com.portability.addresses_service.model.EnviaCancelLabel;
import com.portability.addresses_service.model.EnviaLabel;
import com.portability.addresses_service.repository.EnviaCancelLabelRepository;
import com.portability.addresses_service.repository.EnviaLabelRepository;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
@Slf4j
public class EnviaIntegrationService {

    @Autowired
    private EnviaConfig enviaConfig;

    @Autowired
    private OkHttpClient client;

    @Autowired
    private EnviaLabelRepository enviaLabelRepository;

    @Autowired
    private EnviaCancelLabelRepository enviaCancelLabelRepository;

    private static final float MAXIMUM_PRICE = 150.0f;
    private static final String ALLOWED_COUNTRY = "MX";

    public ZipCodeResponse validateAddress(ZipCodeRequest zipCode) {

        Response response;
        ObjectMapper objectMapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(enviaConfig.getUrlAddressValidation() + zipCode.getZipCode())
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + enviaConfig.getToken())
                .build();
        try {
            response = client.newCall(request).execute();
            JsonNode root = objectMapper.readTree(response.body().string());

            if (root.isEmpty()) {
                log.warn("Address validation failed for zip code.");
                throw new InvalidAddressException("Invalid address for zip code");
            }

            JsonNode object = root.get(0);

            String countryCode = object.path("country").path("code").asText();

            if (!ALLOWED_COUNTRY.equals(countryCode)) {
                log.warn("Address validation failed for unsupported country: {}", countryCode);
                throw new InvalidAddressException("Unsupported country: " + countryCode);
            }

            return ZipCodeResponse.builder()
                    .valid(true)
                    .countryCode(countryCode)
                    .state(object.path("state").path("code").path("2digit").asText())
                    .city(object.path("locality").asText())
                    .build();

        } catch (IOException e) {
            log.error("Error validating address with Envia: {}", e.getMessage());
            throw new RuntimeException("Error validating address with Envia", e);
        }
    }

    public List<EnviaQuoteResponse> getShipmentOptions(CarriersOptionsRequest requestDTO) {

        Response response;
        ObjectMapper objectMapper = new ObjectMapper();
        List<EnviaQuoteResponse> carriersOptions = new ArrayList<>();

        EnviaDimensions dimensions = EnviaDimensions.builder().build();
        EnviaPackages enviaPackages = EnviaPackages.builder()
                .dimensions(dimensions)
                .build();

        Request request = new Request.Builder()
                .url(enviaConfig.getUrlGetCarriers())
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + enviaConfig.getToken())
                .build();
        try {
            response = client.newCall(request).execute();
            JsonNode root = objectMapper.readTree(response.body().string());

            JsonNode dataNode = root.path("data");

            for (JsonNode carrier : dataNode) {

                EnviaCarriersOptionsShipment shipment = EnviaCarriersOptionsShipment.builder()
                        .carrier(carrier.path("name").asText())
                        .build();

                EnviaApiCarriersOptionsRequest enviaApiRequest = EnviaApiCarriersOptionsRequest.builder()
                        .origin(requestDTO.getOrigin())
                        .destination(requestDTO.getDestination())
                        .packages(List.of(enviaPackages))
                        .shipment(shipment)
                        .build();

                RequestBody requestBody = RequestBody.create(
                        objectMapper.writeValueAsString(enviaApiRequest),
                        okhttp3.MediaType.parse("application/json"));

                Request quoteRequest = new Request.Builder()
                        .url(enviaConfig.getUrlShip() + "rate")
                        .post(requestBody)
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", "Bearer " + enviaConfig.getToken())
                        .build();

                Response quoteResponse = client.newCall(quoteRequest).execute();

                JsonNode quoteRoot = objectMapper.readTree(quoteResponse.body().string());

                JsonNode quoteMetaNode = quoteRoot.path("meta");

                if (quoteMetaNode.asText().equals("error")) {
                    continue;
                }

                JsonNode quoteDataNode = quoteRoot.path("data");

                for (JsonNode quote : quoteDataNode) {

                    if ((float) quote.path("totalPrice").asDouble() > MAXIMUM_PRICE) {
                        continue;
                    }

                    JsonNode deliveryDateNode = quote.path("deliveryDate");
                    EnviaDeliveryDate deliveryDate = EnviaDeliveryDate.builder()
                            .date(deliveryDateNode.path("date").asText())
                            .dateDifference(deliveryDateNode.path("dateDifference").asInt())
                            .timeUnit(deliveryDateNode.path("timeUnit").asText())
                            .time(deliveryDateNode.path("time").asText())
                            .build();

                    EnviaQuoteResponse quoteResponseDTO = EnviaQuoteResponse.builder()
                            .carrier(carrier.path("name").asText())
                            .carrierDescription(carrier.path("description").asText())
                            .service(quote.path("service").asText())
                            .serviceDescription(quote.path("serviceDescription").asText())
                            .deliveryEstimate(quote.path("deliveryEstimate").asText())
                            .deliveryDate(deliveryDate)
                            .totalPrice((float) quote.path("totalPrice").asDouble())
                            .basePrice((float) quote.path("basePrice").asDouble())
                            .taxes((float) quote.path("taxes").asDouble())
                            .build();
                    carriersOptions.add(quoteResponseDTO);
                }

            }

            return carriersOptions;
        } catch (IOException e) {
            log.error("Error validating address with Envia: {}", e.getMessage());
            throw new RuntimeException("Error validating address with Envia", e);
        }
    }

    public EnviaLabel createShipmentLabel(String orderId, CreateLabelRequest requestDTO) {

        Response response;
        ObjectMapper objectMapper = new ObjectMapper();

        EnviaDimensions dimensions = EnviaDimensions.builder().build();
        EnviaPackages enviaPackages = EnviaPackages.builder()
                .dimensions(dimensions)
                .build();

        EnviaSettings enviaSettings = EnviaSettings.builder().build();

        EnviaCreateLabelShipment shipment = EnviaCreateLabelShipment.builder()
                .carrier(requestDTO.getCarrier())
                .service(requestDTO.getService())
                .build();

        EnviaApiCreateLabelRequest enviaApiRequest = EnviaApiCreateLabelRequest.builder()
                .origin(requestDTO.getOrigin())
                .destination(requestDTO.getDestination())
                .packages(List.of(enviaPackages))
                .settings(enviaSettings)
                .shipment(shipment)
                .build();

        try {
            RequestBody requestBody = RequestBody.create(
                    objectMapper.writeValueAsString(enviaApiRequest),
                    okhttp3.MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(enviaConfig.getUrlShip() + "generate")
                    .post(requestBody)
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer " + enviaConfig.getToken())
                    .build();
            response = client.newCall(request).execute();
            JsonNode root = objectMapper.readTree(response.body().string());

            JsonNode dataNode = root.path("meta");

            if (dataNode.asText().equals("error")) {
                log.warn("Label creation failed for order id: {}", orderId);
                JsonNode errorNode = root.path("error");
                JsonNode messageNode = errorNode.path("message");

                if (messageNode.asText().contains("Not Enough money")) {
                    throw new NotEnoughMoneyException("Not enough money to create the label");
                } else {
                    throw new RuntimeException("Error creating shipment label with Envia: " + messageNode.asText());
                }
            }

            JsonNode labelDataNode = root.path("data").get(0);

            EnviaLabel enviaLabel = EnviaLabel.builder()
                    .orderId(orderId)
                    .carrier(labelDataNode.path("carrier").asText())
                    .service(labelDataNode.path("service").asText())
                    .shipmentId(labelDataNode.path("shipmentId").asInt())
                    .trackingNumber(labelDataNode.path("trackingNumber").asText())
                    .trackUrl(labelDataNode.path("trackUrl").asText())
                    .label(labelDataNode.path("label").asText())
                    .additionalFiles(objectMapper.convertValue(
                            labelDataNode.path("additionalFiles"),
                            objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)))
                    .totalPrice(labelDataNode.path("totalPrice").asDouble())
                    .currentBalance(labelDataNode.path("currentBalance").asText())
                    .currency(labelDataNode.path("currency").asText())
                    .build();

            EnviaLabel savedLabel = enviaLabelRepository.save(enviaLabel);
            return savedLabel;

        } catch (IOException e) {
            log.error("Error creating shipment label with Envia: {}", e.getMessage());
            throw new RuntimeException("Error creating shipment label with Envia", e);
        }
    }

    public EnviaCancelLabel cancelShipmentLabel(String orderId) {

        Response response;

        EnviaLabel existingLabel = enviaLabelRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("No label found for order id: " + orderId));

        ObjectMapper objectMapper = new ObjectMapper();

        EnviaCancelRequest cancelRequest = new EnviaCancelRequest(
                existingLabel.getCarrier(),
                existingLabel.getTrackingNumber());

        try {
            RequestBody requestBody = RequestBody.create(
                    objectMapper.writeValueAsString(cancelRequest),
                    okhttp3.MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(enviaConfig.getUrlShip() + "cancel")
                    .post(requestBody)
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer " + enviaConfig.getToken())
                    .build();

            response = client.newCall(request).execute();
            JsonNode root = objectMapper.readTree(response.body().string());

            JsonNode metaNode = root.path("meta");

            if (metaNode.asText().equals("error")) {
                log.warn("Label cancellation failed for order id: {}", orderId);
                throw new RuntimeException("Error canceling shipment label with Envia");
            }

            JsonNode dataNode = root.path("data");
            EnviaCancelLabel cancelLabel = EnviaCancelLabel.builder()
                    .orderId(orderId)
                    .carrier(existingLabel.getCarrier())
                    .service(existingLabel.getService())
                    .trackingNumber(existingLabel.getTrackingNumber())
                    .folio(dataNode.path("folio").asText())
                    .balanceReturned(dataNode.path("balanceReturned").asBoolean())
                    .balanceReturnedDate(dataNode.path("balanceReturnedDate").asText().isEmpty() ? null
                            : objectMapper.convertValue(dataNode.path("balanceReturnedDate"), LocalDateTime.class))
                    .build();

            EnviaCancelLabel savedCancelLabel = enviaCancelLabelRepository.save(cancelLabel);
            enviaLabelRepository.delete(existingLabel);

            return savedCancelLabel;

        } catch (IOException e) {
            log.error("Error validating address with Envia: {}", e.getMessage());
            throw new RuntimeException("Error validating address with Envia", e);
        }
    }

    public EnviaTrackingResponse getGeneralTrack(String orderId) {

        Response response;

        EnviaLabel existingLabel = enviaLabelRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("No label found for order id: " + orderId));

        ObjectMapper objectMapper = new ObjectMapper();

        EnviaTrackingRequest trackingRequest = new EnviaTrackingRequest(
                existingLabel.getTrackingNumber());

        try {
            RequestBody requestBody = RequestBody.create(
                    objectMapper.writeValueAsString(trackingRequest),
                    okhttp3.MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(enviaConfig.getUrlShip() + "generaltrack")
                    .post(requestBody)
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer " + enviaConfig.getToken())
                    .build();

            response = client.newCall(request).execute();
            JsonNode root = objectMapper.readTree(response.body().string());

            JsonNode metaNode = root.path("meta");

            if (metaNode.asText().equals("error")) {
                log.warn("General tracking failed for order id: {}", orderId);
                throw new RuntimeException("Error getting general tracking with Envia");
            }

            JsonNode dataNode = root.path("data");

            dataNode.get(0);

            EnviaTrackingResponse trackingResponse = new EnviaTrackingResponse(
                    dataNode.get(0).path("trackUrl").asText(),
                    dataNode.get(0).path("trackUrlSite").asText());

            return trackingResponse;

        } catch (IOException e) {
            log.error("Error validating address with Envia: {}", e.getMessage());
            throw new RuntimeException("Error validating address with Envia", e);
        }
    }

    public EnviaLabel getLabelByOrderId(String orderId) {
        log.info("Fetching EnviaLabel for order id: {}", orderId);
        return enviaLabelRepository.findByOrderId(orderId)
                .orElseThrow(() -> {
                    log.warn("EnviaLabel not found for order id: {}", orderId);
                    return new com.portability.addresses_service.exception.ResourceNotFoundException(
                            "EnviaLabel not found for order id: " + orderId);
                });
    }

}
