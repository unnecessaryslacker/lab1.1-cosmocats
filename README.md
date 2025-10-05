# Cosmo Cats Intergalactic Marketplace — LAB 1.1

**Stack**: Java 21, Spring Boot 3.3.x, MapStruct, Jakarta Bean Validation, springdoc-openapi (Swagger UI).  
**Focus**: OpenAPI контракт, CRUD Product, валідація, RFC 9457 error handling.

## Запуск
```bash
mvn clean spring-boot:run
```

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Приклади cURL
```bash
# Create (201)
curl -s -X POST http://localhost:8080/api/products   -H 'Content-Type: application/json'   -d '{
    "name": "Galaxy Milk Deluxe",
    "description": "Ultra-fat cosmic milk",
    "price": 9.99,
    "currency": "USD",
    "categoryId": "dairy"
  }' | jq

# Validation error (400, Problem Details)
curl -s -X POST http://localhost:8080/api/products   -H 'Content-Type: application/json'   -d '{"name":"Plain Milk","price":9.99,"currency":"USD","categoryId":"dairy"}' | jq

# List (200)
curl -s http://localhost:8080/api/products | jq
```

## Структура
- `domain/` — доменні моделі
- `dto/` — DTO + валідація (`@CosmicWordCheck`)
- `mapper/` — MapStruct
- `repository/` — in-memory (mock)
- `service/` — бізнес-логіка
- `web/` — REST-контролер + RFC 9457 `ErrorHandler`
- `resources/api-specs/cosmo-cats-product.yml` — OpenAPI контракт
