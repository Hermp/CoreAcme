# Spring Boot Hexagonal Architecture Example

This project demonstrates a Spring Boot application using hexagonal architecture (also known as ports and adapters) with Cucumber for BDD testing.

## Project Structure

The project follows a hexagonal architecture with three main layers:

### Domain Layer
- Contains the core business logic and entities
- Independent of external frameworks and technologies
- Located in `src/main/java/org/ph/domain`

### Application Layer
- Contains use cases and ports (interfaces)
- Coordinates between the domain and infrastructure layers
- Located in `src/main/java/org/ph/application`

### Infrastructure Layer
- Contains adapters for external dependencies (REST controllers, repositories, etc.)
- Implements the ports defined in the application layer
- Located in `src/main/java/org/ph/infrastructure`

## Use Case: Product Management

The project implements a complete use case for product management with the following operations:
- Create a product
- Retrieve a product by ID
- Retrieve all products
- Update a product
- Delete a product

## Technologies Used

- Spring Boot 3.2.0
- Spring Web for REST API
- Hibernate Validator for input validation
- Jackson for JSON processing
- Lombok for reducing boilerplate code
- Cucumber for BDD testing
- JUnit 5 for unit testing

## Running the Application

To run the application:

```bash
./gradlew bootRun
```

The application will start on port 8080.

## API Endpoints

- `POST /api/products` - Create a new product
- `GET /api/products/{id}` - Get a product by ID
- `GET /api/products` - Get all products
- `PUT /api/products/{id}` - Update a product
- `DELETE /api/products/{id}` - Delete a product

## Running the Tests

To run the Cucumber tests:

```bash
./gradlew test
```

The test reports will be generated in `target/cucumber-reports`.

## Hexagonal Architecture Benefits

- **Separation of concerns**: Each layer has a specific responsibility
- **Testability**: The domain logic can be tested independently of external dependencies
- **Flexibility**: External dependencies can be replaced without affecting the domain logic
- **Maintainability**: Changes in one layer don't affect other layers