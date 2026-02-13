# Refactorización del Servicio de Direcciones

## Resumen de Cambios

### 1. Sistema de Filtrado y Paginación

Se ha implementado un sistema completo de filtrado y paginación similar al servicio de órdenes:

#### Nuevas Clases Creadas:

1. **AddressFilterRequest.java** - DTO para los criterios de filtrado
   - `search`: Búsqueda general en calle, ciudad, estado o país
   - `street`: Filtro por calle
   - `city`: Filtro por ciudad
   - `state`: Filtro por estado/provincia
   - `postalCode`: Filtro por código postal
   - `country`: Filtro por país
   - `orderId`: Filtro por ID de orden

2. **PagedResponse.java** - DTO para respuestas paginadas
   - Contiene información de paginación (número de página, tamaño, total de elementos, etc.)

3. **AddressSpecification.java** - Especificación JPA para filtrado dinámico
   - Implementa Specification<Address> para queries dinámicas
   - Soporta búsqueda parcial (LIKE) en campos de texto
   - Búsqueda exacta para orderId

#### Actualizaciones al Repositorio:

- Agregado `JpaSpecificationExecutor<Address>` para soportar queries dinámicas

#### Actualizaciones al Servicio:

- Nuevo método `getAllAddresses(AddressFilterRequest filter, Pageable pageable)` con filtrado y paginación

#### Actualizaciones al Controlador:

El endpoint principal `GET /api/addresses` ahora soporta los siguientes parámetros:

**Filtros:**
- `search` - Búsqueda general en calle, ciudad, estado o país
- `street` - Filtro por calle
- `city` - Filtro por ciudad
- `state` - Filtro por estado
- `postalCode` - Filtro por código postal
- `country` - Filtro por país
- `orderId` - Filtro por ID de orden

**Paginación:**
- `page` - Número de página (0-indexed, default: 0)
- `size` - Tamaño de página (default: 20)
- `sortBy` - Campo para ordenar (default: "createdAt")
- `sortDirection` - Dirección de ordenamiento: "asc" o "desc" (default: "desc")

### 3. Migración de Base de Datos

Se ha creado un script de migración SQL:
- **Archivo**: `src/main/resources/db/migration/V1__rename_portability_id_to_order_id.sql`
- **Acción**: Renombra la columna `portability_id` a `order_id` en la tabla `addresses`

## Ejemplos de Uso

### Obtener todas las direcciones con filtros y paginación:

```bash
GET /api/addresses?search=Madrid&page=0&size=10&sortBy=city&sortDirection=asc
```

### Filtrar por ciudad específica:

```bash
GET /api/addresses?city=Barcelona&page=0&size=20
```

### Filtrar por orderId:

```bash
GET /api/addresses?orderId=12345
```

### Obtener dirección por Order ID:

```bash
GET /api/addresses/order/12345
```

## Pasos Siguientes

1. **Ejecutar el script de migración de BD** en tu base de datos antes de desplegar
2. **Compilar el proyecto**: `mvn clean install`
3. **Ejecutar tests**: `mvn test`
4. **Desplegar** la nueva versión

## Cambios de API (Breaking Changes)

⚠️ **IMPORTANTE**: Los siguientes endpoints han cambiado:

- ❌ Eliminado: `GET /api/addresses/portability/{portabilityId}`
- ✅ Nuevo: `GET /api/addresses/order/{orderId}`
- 🔄 Modificado: `GET /api/addresses` ahora retorna `PagedResponse<AddressDTO>` en lugar de `List<AddressDTO>`

Los clientes que consuman esta API deberán actualizarse para usar `orderId` en lugar de `portabilityId`.
