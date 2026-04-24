# Product Service

A Spring Boot for managing product information with REST API endpoints, exception handling, and comprehensive testing.

## Table of Contents
- [Project Overview](#project-overview)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Exception Handling](#exception-handling)
- [Database Setup](#database-setup)
- [Contributing](#contributing)
- [License](#license)

---

## Project Overview

The **Product Service** is a Spring Boot REST API that provides endpoints for managing product information. It includes features such as:
- Create new products
- Retrieve product details by ID
- Global exception handling
- Comprehensive unit and integration tests
- Database migration using Flyway
- Lombok annotations for cleaner code

---

## Technologies Used

| Technology | Version | Purpose |
|-----------|---------|---------|
| **Java** | 17+ | Programming language |
| **Spring Boot** | 3.x | Framework |
| **Spring Data JPA** | 3.x | Database access |
| **MySQL** | 8.0+ | Database |
| **Flyway** | 9.x | Database migration |
| **Mockito** | 5.x | Unit testing |
| **JUnit 5** | 5.x | Testing framework |
| **Lombok** | 1.18.x | Code generation |
| **Maven** | 3.8+ | Build tool |

---

##  Project Structure

```bash
ProductService/
├── src/
│   ├── main/
│   │   ├── java/com/prakash/productservice/
│   │   │   ├── ProductServiceApplication.java   # Main application class
│   │   │   ├── controller/
│   │   │   │   └── ProductController.java       # REST API endpoints
│   │   │   ├── service/
│   │   │   │   ├── ProductService.java          # Service interface
│   │   │   │   └── ProductServiceImpl.java      # Service implementation
│   │   │   ├── repository/
│   │   │   │   └── ProductRepository.java       # JPA repository
│   │   │   ├── entity/
│   │   │   │   └── Product.java                 # JPA entity
│   │   │   ├── dto/
│   │   │   │   └── ProductDto.java              # Data Transfer Object
│   │   │   └── exception/
│   │   │       ├── ProductCustomException.java  # Custom exception
│   │   │       ├── ErrorMessage.java            # Error response model
│   │   │       └── RestResponseEntityHandler.java # Global exception handler
│   │   └── resources/
│   │       ├── application.yaml                 # Application configuration
│   │       └── db/migration/
│   │           └── V1_create_products_table.sql # Database schema (Flyway)
│   └── test/
│       └── java/com/prakash/productservice/
│           ├── controller/
│           │   └── ProductControllerTest.java   # Controller tests
│           └── service/
│               └── ProductServiceTest.java      # Service tests
├── pom.xml                                     # Maven dependencies
├── README.md                                   # Project documentation
└── .gitignore                                  # Git ignore rules

---
```
## Prerequisites

Before you begin, ensure you have the following installed:

1. **Java Development Kit (JDK) 17 or higher**
   - [Download JDK](https://www.oracle.com/java/technologies/downloads/)
   - Verify: `java -version`

2. **Apache Maven 3.8+**
   - [Download Maven](https://maven.apache.org/download.cgi)
   - Verify: `mvn -version`

3. **MySQL Server 8.0+**
   - [Download MySQL](https://dev.mysql.com/downloads/mysql/)
   - Service should be running

4. **Git** (for version control)
   - [Download Git](https://git-scm.com/download/win)

5. **IDE** (Optional but recommended)
   - IntelliJ IDEA
   - VS Code
   - Eclipse

---

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/ProductService.git
cd ProductService
```
### 2. Build the Project
#### Using Maven:
```bash 
mvn clean install
``` 
This will:

- Clean the target directory
- Download dependencies
- Compile the source code
- Run tests
- Package the application

### verify the build was successful:
```bash
BUILD SUCCESS
```
---
## Configuration

Database Configuration

Update src/main/resources/application.yaml with your database credentials:
```bash
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/{{DB_NAME}}?useSSL=false&serverTimezone=UTC
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
    show-sql: false
  
  flyway:
    enabled: true
    locations: classpath:db/migration
```
### Running the Application
#### Using Maven:
```bash
mvn spring-boot:run
```
#### Using the JAR file:
```bash
java -jar target/ProductService-0.0.1-SNAPSHOT.jar
```
#### Using IDE
- Open the project in your IDE
- Run the main class: `ProductServiceApplication.java`
- Ensure the application starts without errors and listens on port 8080
- You should see a message like:
```bash
Started ProductServiceApplication in 5.123 seconds (JVM running for 6.456
seconds)
```
---
### API Endpoints
#### Add Product
- **URL**: `/api/v1/product/add`
- **Method**: `POST`
- **Request Body**:
```json
{
  "name": "Product Name",
  "description": "Product Description",
  "price": 99.99,
  "quantity": 4
}
```
- **Response(Success)**:
```json
1
```
- Status: `200 OK`
---
#### Get Product by ID
- **URL**: `/api/v1/product/{id}`
- **Method**: `GET`
- **Response(Success)**:
- **Status**: `200 OK`
```json
{
  "id": 1,
  "name": "Product Name",
  "description": "Product Description",
  "price": 99.99,
  "quantity": 4
}
```
- **Response(Error)**:
- **Status**: `404 Not Found`
```json
{
  "errorCode": "PRODUCT_NOT_FOUND",
  "errorMessage": "Product not found 1"

}
```
---
### Testing
#### Run All Tests
```bash
mvn test
```
#### Run Specific Test Class
```bash
mvn test -Dtest=ProductServiceTest
mvn test -Dtest=ProductControllerTest
```
#### Run Specific Test Method
```bash
mvn test -Dtest=ProductServiceTest#testSaveProduct_Success
```
#### Test classes overview:

ProductServiceTest.java

Tests the service layer:

- ✅ `testSaveProduct_Success` - Verify product is saved with correct ID
- ✅ `testGetProductById_Success` - Verify product retrieval with all fields
- ✅ `testGetProductById_ProductNotFound` - Verify exception when product doesn't exist

ProductControllerTest.java

Tests the REST controller:

- ✅ `testSaveProduct_Success` - Verify API endpoint saves product
- ✅ `testGetProductById_Success` - Verify API endpoint retrieves product
- ✅ `testGetProductById_ProductNotFound` - Verify API handles not found gracefully
---
### Exception Handling
The application uses Global Exception Handling via RestResponseEntityHandler.java:
#### ProductCustomException
Thrown when a product is not found:
```bash
throw new ProductCustomException(
     "Product not found  " + id,
      "Product_not_found"
);  
```
#### Responses
- **Error Response**:
```json
{
  "errorCode": "PRODUCT_NOT_FOUND",
  "errorMessage": "Product not found 1"
}
```
- **Status**: `404 Not Found`
#### Error Message Forat
```java
@Builder
public class ErrorMessage {
  private String errorCode;
  private String message;
}
  ```
## Database Setup
#### MySQL Database
1. Create a new database:
```sql
CREATE DATABASE product_db;
``` 
2. The products table structure:
```sql
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL
);
```
## Contributing
#### Steps to contribute:

1. Fork the repository
```bash
git clone https://github.com/yourusername/ProductService.git
```
2. Create a new branch
```bash
git checkout -b feature/your-feature-name
```
3. Make your changes and commit
```bash
git add .
git commit -m "Add your commit message"
```
4. Push to your fork
```bash
git push origin feature/your-feature-name
```
5. Create a pull request on GitHub
- Ensure all tests pass: mvn test
- Add descriptive commit messages
- Reference any related issues

coding standards:
- Follow Java naming conventions
- Use Lombok annotations for boilerplate code
- Write unit tests for new features
- Maintain test coverage above 80%

---
### Troubleshooting
#### Common Issues:
1. **Database Connection Errors**

Solution:
```bash
# Verify MySQL is running
mysql -u root -p

# Check application.yaml database URL and credentials
# Ensure database 'productdb' exists
```
2. Issue: Maven Build Fails 

Solution:
```bash
# Clear Maven cache
mvn clean install -U

# Check Java version
java -version

# Verify Maven configuration
mvn -v
```
3. **Port 8080 Already in Use**

Solution: Change the server port in application.yaml
```yaml
server:
  port: 8081
```
4. **Tests Fail**

Solution:
```bash
# Run tests with verbose output
mvn test -X

# Check if MySQL is running for integration tests
# Verify test data setup in ProductServiceTest.java
```
---
### Future Enhancements
- Add product update endpoint (PUT)
- Add product delete endpoint (DELETE)
- Implement pagination for product listing
- Add product search functionality
- Implement JWT authentication
- Add API documentation (Swagger/OpenAPI)
- Implement caching (Redis)
- Add logging (SLF4J + Logback)
- Containerize with Docker
- Deploy to cloud (AWS/Azure/GCP)
---
## License

- This project is licensed under the MIT License - see the LICENSE file for details.
---
### Contact and Support

For questions, issues, or contributions, please contact:
- **Name**: Prakash Subedi
- **Email**:dizprakash@gmail.com
- **GitHub**: []
---
### Change Log
#### Version 1.0.0 - Initial Release
- ✅ Create product endpoint
- ✅ Get product by ID endpoint
- ✅ Global exception handling
- ✅ Unit tests with Mockito
---
### Author
- **Name**: Prakash 
---




