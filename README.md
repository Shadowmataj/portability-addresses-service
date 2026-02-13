# Addresses Service

A Spring Boot microservice for managing customer addresses and shipping integrations with Envia (Mexican shipping platform). This service is part of a larger e-commerce platform and provides comprehensive address management capabilities along with shipping label generation and tracking.

## ✨ Features

### Address Management
- **CRUD Operations**: Complete create, read, update, and delete operations for customer addresses
- **Advanced Filtering**: Filter addresses by type, customer ID, location (street, city, state, postal code, country)
- **Full-Text Search**: Search across multiple address fields simultaneously
- **Pagination & Sorting**: Efficient data retrieval with customizable pagination and sorting options
- **Address Types**: Support for different address types (SHIPPING, BILLING, etc.)

### Envia Integration
- **Address Validation**: Validate Mexican addresses by postal code
- **Carrier Options**: Get available shipping carriers and quotes for shipments
- **Label Generation**: Create shipping labels for orders
- **Label Cancellation**: Cancel previously created shipping labels
- **Shipment Tracking**: Track shipments using order IDs
- **Webhook Support**: Handle Envia webhook events for shipment updates

### Technical Features
- **Spring Cloud Integration**: Service discovery with Eureka
- **OpenFeign Client**: Declarative REST client for microservice communication
- **Dynamic Queries**: JPA Specifications for flexible filtering
- **AOP Logging**: Aspect-oriented logging for better observability
- **API Documentation**: Interactive Swagger UI documentation
- **Validation**: Comprehensive input validation with Bean Validation
- **Exception Handling**: Centralized error handling

## 🛠 Technologies

- **Java 25** - Latest Java features
- **Spring Boot 4.0.2** - Application framework
- **Spring Cloud 2025.1.0** - Microservices infrastructure
  - Eureka Client - Service discovery
  - OpenFeign - Declarative REST clients
- **Spring Data JPA** - Database access layer
- **PostgreSQL** - Relational database
- **Hibernate** - ORM implementation
- **Lombok** - Boilerplate code reduction
- **SpringDoc OpenAPI 2.3.0** - API documentation
- **OkHttp** - HTTP client for external API calls
- **Maven** - Dependency management

## 🏗 Architecture

This service follows a layered architecture pattern:

```
┌─────────────────────────────────────────────┐
│            REST Controllers                  │
│  (AddressController, EnviaIntegrationController)
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│          Service Layer                       │
│  (AddressService, EnviaIntegrationService)  │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│      Repository Layer (JPA)                  │
│  (AddressRepository, EnviaLabelRepository)  │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│          PostgreSQL Database                 │
└─────────────────────────────────────────────┘

         External Integration
┌─────────────────────────────────────────────┐
│          Envia API                           │
│  (Address Validation, Shipping, Tracking)   │
└─────────────────────────────────────────────┘
```

## 📦 Prerequisites

- **Java 25** or higher
- **Maven 3.8+**
- **PostgreSQL 13+**
- **Envia API Account** (for shipping integration features)
- **Eureka Server** (for service discovery, if using microservices architecture)

## 🚀 Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd addresses-service
```

2. **Create the PostgreSQL database**
```sql
CREATE DATABASE addresses_db;
```

3. **Update application properties**

Edit `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/addresses_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. **Configure Envia API credentials**
```properties
envia.api.token=your_envia_api_token
```

5. **Build the project**
```bash
./mvnw clean install
```

6. **Run the application**
```bash
./mvnw spring-boot:run
```

The service will start on port **8084** by default.

## ⚙️ Configuration

### Main Configuration Properties

```properties
# Server Configuration
server.port=8084
spring.application.name=addresses-service

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/addresses_db
spring.datasource.username=postgres
spring.datasource.password=your_password

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Envia API Configuration
envia.api.token=your_envia_token
envia.api.url-address-validation=<ENVIA_ADDRESS_VALIDATION>
envia.api.url-get-carriers=<ENVIA_GETCARRIERS_URL>
envia.api.url-ship=<ENVIA_URL_SHIP>

# Swagger UI
springdoc.swagger-ui.path=/swagger-ui.html
```

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8084/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8084/api-docs

### Main Endpoints

#### Address Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/addresses` | Get all addresses (with filtering & pagination) |
| GET | `/api/addresses/{id}` | Get address by ID |
| POST | `/api/addresses` | Create a new address |
| PUT | `/api/addresses/{id}` | Update an address |
| DELETE | `/api/addresses/{id}` | Delete an address |
| GET | `/api/addresses/customer/{customerId}` | Get addresses by customer ID |

#### Envia Integration

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/envia-integration/validate-address` | Validate a Mexican address by postal code |
| POST | `/api/envia-integration/carriers-options` | Get available carriers and shipping quotes |
| POST | `/api/envia-integration/create-label/{orderId}` | Create a shipping label for an order |
| POST | `/api/envia-integration/cancel-label/{orderId}` | Cancel a shipping label |
| POST | `/api/envia-integration/general-track/{orderId}` | Track a shipment |
| GET | `/api/envia-integration/label/{orderId}` | Get label details by order ID |

## 💡 Usage

### Example: Create an Address

```bash
curl -X POST http://localhost:8084/api/addresses \
  -H "Content-Type: application/json" \
  -d '{
    "addressType": "SHIPPING",
    "customerId": 123,
    "street": "Reforma Avenue",
    "number": "222",
    "district": "Juárez",
    "city": "Mexico City",
    "state": "CDMX",
    "postalCode": "06600",
    "country": "Mexico",
    "reference": "Near the Angel of Independence"
  }'
```

### Example: Get Addresses with Filtering

```bash
# Get all shipping addresses in Mexico City, page 0, 10 items per page
curl "http://localhost:8084/api/addresses?addressType=SHIPPING&city=Mexico%20City&page=0&size=10&sortBy=createdAt&sortDirection=desc"
```

### Example: Validate Address (Envia)

```bash
curl -X POST http://localhost:8084/api/envia-integration/validate-address \
  -H "Content-Type: application/json" \
  -d '{
    "zipCode": "06600"
  }'
```

### Example: Get Shipping Quotes

```bash
curl -X POST http://localhost:8084/api/envia-integration/carriers-options \
  -H "Content-Type: application/json" \
  -d '{
    "origin": {
      "postalCode": "01000",
      "city": "Mexico City",
      "state": "CDMX",
      "country": "Mexico"
    },
    "destination": {
      "postalCode": "44100",
      "city": "Guadalajara",
      "state": "Jalisco",
      "country": "Mexico"
    },
    "packages": [{
      "weight": 1.5,
      "height": 10,
      "width": 20,
      "length": 30
    }]
  }'
```

## 🗃 Database Schema

### Address Entity

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key (auto-generated) |
| address_type | VARCHAR(20) | Type of address (SHIPPING, BILLING, etc.) |
| customer_id | BIGINT | Foreign key to customer |
| street | VARCHAR(200) | Street name |
| number | VARCHAR(20) | Street number |
| district | VARCHAR(100) | District/neighborhood |
| city | VARCHAR(100) | City name |
| state | VARCHAR(100) | State/province |
| postal_code | VARCHAR(20) | Postal/ZIP code |
| country | VARCHAR(100) | Country name |
| reference | VARCHAR(500) | Additional reference information |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

## 📂 Project Structure

```
src/
├── main/
│   ├── java/com/portability/addresses_service/
│   │   ├── AddressesServiceApplication.java     # Main application class
│   │   ├── aspect/
│   │   │   └── LoggingAspect.java              # AOP logging
│   │   ├── config/
│   │   │   ├── EnviaConfig.java                # Envia API configuration
│   │   │   ├── OkHttpConfig.java               # HTTP client configuration
│   │   │   └── OpenAPIConfig.java              # Swagger/OpenAPI configuration
│   │   ├── controller/
│   │   │   ├── AddressController.java          # Address CRUD endpoints
│   │   │   ├── EnviaIntegrationController.java # Envia integration endpoints
│   │   │   └── EnviaIntegrationWebhookController.java # Webhook handler
│   │   ├── dto/
│   │   │   ├── AddressRequest.java             # Address creation/update DTO
│   │   │   ├── AddressResponse.java            # Address response DTO
│   │   │   ├── AddressFilterRequest.java       # Filtering criteria DTO
│   │   │   ├── PagedResponse.java              # Pagination wrapper
│   │   │   └── ...                             # Other DTOs
│   │   ├── enm/
│   │   │   └── AddressType.java                # Address type enum
│   │   ├── exception/
│   │   │   └── ...                             # Custom exceptions
│   │   ├── mapper/
│   │   │   └── AddressMapper.java              # Entity-DTO mappers
│   │   ├── model/
│   │   │   ├── Address.java                    # Address entity
│   │   │   └── EnviaLabel.java                 # Envia label entity
│   │   ├── repository/
│   │   │   ├── AddressRepository.java          # Address data access
│   │   │   └── EnviaLabelRepository.java       # Label data access
│   │   ├── service/
│   │   │   ├── AddressService.java             # Address business logic
│   │   │   └── EnviaIntegrationService.java    # Envia integration logic
│   │   └── specification/
│   │       └── AddressSpecification.java       # JPA dynamic queries
│   └── resources/
│       ├── application.properties               # Application configuration
│       └── db/migration/                        # Flyway migrations (if used)
└── test/
    └── java/com/portability/addresses_service/
        └── AddressesServiceApplicationTests.java
```

## 🔍 Key Components

### AddressSpecification
Implements JPA Specifications for dynamic query building:
- Supports partial text search across multiple fields
- AND/OR combinations for complex filtering
- Type-safe query construction

### LoggingAspect
Provides cross-cutting logging concerns:
- Logs method entry/exit
- Logs execution time
- Logs method parameters and return values

### EnviaIntegrationService
Handles all Envia API communications:
- Address validation using Mexican postal codes
- Carrier comparison and quote generation
- Label creation and cancellation
- Shipment tracking

## 🧪 Testing

Run tests with:
```bash
./mvnw test
```