# Product Service

A Spring Boot REST API for product management with JWT security, role-based permissions, product search, email notifications, RabbitMQ events, database migrations, API documentation, and monitoring support.

## Features

- Product creation and lookup by ID
- Product update and delete operations
- Paginated product listing and full-text search by product name or description
- Request validation for products, users, authentication, and mail
- User registration and JWT authentication
- Role and permission based authorization
- Email sending endpoint
- Product-created event publishing through RabbitMQ
- RabbitMQ listener for product-created notification emails
- Dead-letter queue support for failed RabbitMQ messages
- MySQL persistence with Spring Data JPA
- Flyway database migrations
- Swagger/OpenAPI documentation
- Spring Boot Actuator health and Prometheus metrics
- Docker Compose setup for MySQL, RabbitMQ, Prometheus, Grafana, and the application
- Unit and controller tests, plus Testcontainers support

## Tech Stack

| Technology | Purpose |
| --- | --- |
| Java 21 | Runtime and language |
| Spring Boot 4.0.5 | Application framework |
| Spring Web | REST API |
| Spring Data JPA | Database access |
| Spring Security | JWT authentication and authorization |
| MySQL 8 | Database |
| Flyway | Database migrations |
| RabbitMQ | Product event messaging |
| Spring Mail | Email delivery |
| Springdoc OpenAPI | Swagger UI and API docs |
| Actuator + Micrometer Prometheus | Health checks and metrics |
| Docker Compose | Local service orchestration |
| JUnit 5, Mockito, Testcontainers | Testing |
| Lombok | Boilerplate reduction |
| Maven | Build tool |

## Project Structure

```text
ProductService/
+-- src/main/java/com/prakash/product_service/
|   +-- config/          # OpenAPI and RabbitMQ configuration
|   +-- controller/      # Product, user, and mail REST controllers
|   +-- controller/api/  # OpenAPI interface contracts
|   +-- dto/             # Request and response DTOs
|   +-- entity/          # JPA entities, roles, and permissions
|   +-- event/           # Product event payloads
|   +-- exception/       # Custom exceptions and global handler
|   +-- messaging/       # RabbitMQ publisher and listener
|   +-- repository/      # Spring Data repositories
|   +-- security/        # JWT filter, service, and security config
|   +-- service/         # Business services
+-- src/main/resources/
|   +-- application.yaml
|   +-- db/migration/    # Flyway migrations
+-- src/test/java/       # Unit, controller, messaging, and integration tests
+-- docker-compose.yml
+-- Dockerfile
+-- prometheus.yml
+-- .env.example
+-- pom.xml
```

## Prerequisites

- Java 21
- Maven 3.9+
- Docker and Docker Compose, recommended for local infrastructure
- MySQL 8, if running without Docker Compose
- RabbitMQ, if running messaging without Docker Compose

## Configuration

The application imports environment values from `.env` when present:

```bash
cp .env.example .env
```

Important variables:

| Variable | Description | Default |
| --- | --- | --- |
| `DB_URL` | JDBC URL for MySQL | `jdbc:mysql://localhost:3306/product` |
| `DB_USERNAME` | Database username | Required |
| `DB_PASSWORD` | Database password | Required |
| `JWT_SECRET` | Secret used to sign JWT tokens | Required |
| `JWT_EXPIRATION_MS` | JWT lifetime in milliseconds | `3600000` |
| `SERVER_PORT` | Application port | `8090` |
| `MAIL_HOST` | SMTP host | `smtp.gmail.com` |
| `MAIL_PORT` | SMTP port | `587` |
| `MAIL_USERNAME` | SMTP username | Configured from env |
| `MAIL_PASSWORD` | SMTP password or app password | Configured from env |
| `RABBITMQ_HOST` | RabbitMQ host | `localhost` |
| `RABBITMQ_PORT` | RabbitMQ port | `5672` |
| `PRODUCT_NOTIFICATION_EMAIL` | Email notified when a product is created | Empty |

Use a strong `JWT_SECRET` with at least 32 characters.

## Running Locally

### Run With Docker Compose

```bash
docker compose up --build
```

Services started by Docker Compose:

| Service | URL |
| --- | --- |
| Product Service | `http://localhost:8090` |
| RabbitMQ Management | `http://localhost:15672` |
| Prometheus | `http://localhost:9090` |
| Grafana | `http://localhost:3000` |
| MySQL | `localhost:3307` |

### Run With Maven

Start MySQL and RabbitMQ first, then run:

```bash
mvn spring-boot:run
```

### Build The Jar

```bash
mvn clean package
java -jar target/ProductService-0.0.1-SNAPSHOT.jar
```

## Authentication

Register a user:

```http
POST /api/users
Content-Type: application/json
```

```json
{
  "username": "admin",
  "password": "password",
  "email": "admin@example.com",
  "role": "ROLE_ADMIN"
}
```

Authenticate:

```http
POST /api/users/authenticate
Content-Type: application/json
```

```json
{
  "username": "admin",
  "password": "password"
}
```

The response body is the JWT token. Use it on secured requests:

```http
Authorization: Bearer <token>
```

Available roles:

| Role | Permissions |
| --- | --- |
| `ROLE_ADMIN` | `ADD`, `VIEW`, `VIEW_ALL` |
| `ROLE_STAFF` | `VIEW`, `VIEW_ALL` |
| `ROLE_USER` | `VIEW` |

## API Endpoints

### User API

| Method | Endpoint | Auth | Description |
| --- | --- | --- | --- |
| `POST` | `/api/users` | Public | Register a user |
| `POST` | `/api/users/authenticate` | Public | Authenticate and receive JWT |
| `GET` | `/api/users` | JWT | List users |
| `GET` | `/api/users/{userName}` | JWT | Get user by username |

### Product API

| Method | Endpoint | Permission | Description |
| --- | --- | --- | --- |
| `POST` | `/api/v1/product/add` | `ADD` | Create a product |
| `GET` | `/api/v1/product/{id}` | `VIEW` or `VIEW_ALL` | Get product by ID |
| `GET` | `/api/v1/product/search?keyword=phone&page=0&size=10` | `VIEW` or `VIEW_ALL` | Search or list products |
| `PUT` | `/api/v1/product/{id}` | `ADD` | Update a product |
| `DELETE` | `/api/v1/product/{id}` | `ADD` | Delete a product |

Create product request:

```json
{
  "name": "Product Name",
  "description": "Product Description",
  "price": 99.99,
  "quantity": 4
}
```

Search response:

```json
{
  "content": [
    {
      "name": "Product Name",
      "description": "Product Description",
      "price": 99.99,
      "quantity": 4
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "last": true
}
```

### Mail API

| Method | Endpoint | Permission | Description |
| --- | --- | --- | --- |
| `POST` | `/api/v1/mail/send` | `ADD` | Send an email |

Mail request:

```json
{
  "to": "user@example.com",
  "subject": "Hello",
  "message": "Mail body"
}
```

## Documentation And Monitoring

| Feature | Endpoint |
| --- | --- |
| Swagger UI | `/swagger-ui.html` |
| OpenAPI JSON | `/v3/api-docs` |
| Health | `/actuator/health` |
| Prometheus metrics | `/actuator/prometheus` |

Swagger endpoints are public. Other application endpoints require JWT unless explicitly listed as public.

## Error Responses

API errors use a consistent response body:

```json
{
  "message": "Request validation failed",
  "errorCode": "VALIDATION_FAILED",
  "status": 400,
  "path": "/api/v1/product/add",
  "timestamp": "2026-05-30T10:15:30Z",
  "errors": {
    "name": "Product name is required"
  }
}
```

## Database

Flyway migrations are stored in `src/main/resources/db/migration`.

Current migrations:

- `V1_create_products_table.sql` creates the `products` table.
- `V2_add_product_search_index.sql` adds a MySQL full-text index on `name` and `description`.

Product table:

```sql
CREATE TABLE products (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(500),
  price DECIMAL(12,2) NOT NULL,
  quantity INTEGER NOT NULL,
  created_at DATE,
  updated_at DATE
);
```

## RabbitMQ Events

When a product is created, the service publishes a `ProductCreatedEvent` to RabbitMQ.

Default messaging names:

| Name | Default |
| --- | --- |
| Exchange | `product.exchange` |
| Queue | `product.created.queue` |
| Routing key | `product.created` |
| Dead-letter exchange | `product.dlx` |
| Dead-letter queue | `product.created.dlq` |
| Dead-letter routing key | `product.created.dead` |

If `PRODUCT_NOTIFICATION_EMAIL` is configured, the RabbitMQ listener sends an email notification for created products.

## Testing

Run all tests:

```bash
mvn test
```

Run selected tests:

```bash
mvn test -Dtest=ProductServiceTest
mvn test -Dtest=ProductControllerTest
mvn test -Dtest=MailControllerTest
mvn test -Dtest=ProductEventPublisherTest
mvn test -Dtest=ProductEventListenerTest
```

## Troubleshooting

| Problem | Check |
| --- | --- |
| Database connection fails | Confirm MySQL is running and `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` are correct |
| JWT requests return `401` | Confirm the `Authorization: Bearer <token>` header is present and the token is not expired |
| Product creation returns `403` | Use a user role with the `ADD` permission |
| Search returns database errors | Confirm Flyway migration `V2_add_product_search_index.sql` ran successfully |
| Mail does not send | Confirm SMTP credentials and app password settings |
| RabbitMQ events are not consumed | Confirm RabbitMQ is running and listener auto-startup is enabled |

## Author

Prakash Subedi
