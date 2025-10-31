# Hotel Management System

A comprehensive full-stack hotel management system built with Spring Boot and PostgreSQL, featuring Keycloak authentication, complete REST APIs, and audit logging.

## Project Structure

```
hotelmanagement/
├── src/
│   ├── main/
│   │   ├── java/com/thanhhoa/hotelmanagement/
│   │   │   ├── audit/          # AOP-based audit logging
│   │   │   ├── configuration/   # Security and OpenAPI config
│   │   │   ├── controller/      # REST API controllers
│   │   │   ├── dto/            # Request/Response DTOs (Java records)
│   │   │   ├── entity/         # JPA entities
│   │   │   ├── exception/      # Custom exceptions and global handler
│   │   │   ├── mapper/         # Entity-DTO mappers
│   │   │   ├── repository/     # Spring Data JPA repositories
│   │   │   └── service/        # Business logic services
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── Dockerfile
└── pom.xml
```

## Technologies Used

- Java 17
- Spring Boot 3.5.7
- PostgreSQL 15
- Keycloak 23.0.0
- Spring Data JPA
- Spring Security
- Spring AOP (for audit logging)
- Swagger/OpenAPI 3.0
- Lombok
- Docker & Docker Compose

## Features

### Core Functionality

- User Management (Admin, Staff, Guest roles)
- Guest Profile Management
- Staff Management with departments
- Room Management (Multiple room types and statuses)
- Reservation System with conflict detection
- Payment Processing with multiple payment methods
- Automatic Invoice Generation
- Comprehensive Audit Logging

### Technical Features

- RESTful API design
- Java Records for DTOs
- AOP-based audit logging
- Global exception handling
- Request validation
- Swagger UI documentation
- Docker containerization
- Database migrations

## Database Schema

The system includes the following main entities:

- **users**: User accounts with roles
- **guests**: Guest profiles linked to users
- **staff**: Staff members with departments
- **rooms**: Hotel rooms with types and status
- **reservations**: Room bookings with date validation
- **payments**: Payment records with methods
- **invoices**: Auto-generated invoices
- **audit_logs**: Complete audit trail

## Getting Started

### Prerequisites

- Docker and Docker Compose
- Java 17 (for local development)
- Maven 3.9+ (for local development)

### Running with Docker Compose

1. Start all services:

```bash
docker-compose up -d
```

This will start:

- PostgreSQL database (port 5432)
- Keycloak server (port 8180)
- Hotel Management Backend (port 8080)

2. Wait for all services to be healthy:

```bash
docker-compose ps
```

3. Access the services:

- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Keycloak Admin: http://localhost:8180 (admin/admin123)

### Running Locally

1. Start PostgreSQL and Keycloak:

```bash
docker-compose up -d postgres keycloak
```

2. Build and run the backend:

```bash
cd hotelmanagement
mvn clean install
mvn spring-boot:run
```

## API Endpoints

### User Management

- `POST /api/users` - Create user
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username
- `GET /api/users/role/{role}` - Get users by role
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Room Management

- `POST /api/rooms` - Create room
- `GET /api/rooms` - Get all rooms
- `GET /api/rooms/{id}` - Get room by ID
- `GET /api/rooms/available` - Get available rooms
- `GET /api/rooms/status/{status}` - Get rooms by status
- `GET /api/rooms/type/{type}` - Get rooms by type
- `PUT /api/rooms/{id}` - Update room
- `PATCH /api/rooms/{id}/status` - Update room status
- `DELETE /api/rooms/{id}` - Delete room

### Reservation Management

- `POST /api/reservations` - Create reservation
- `GET /api/reservations` - Get all reservations
- `GET /api/reservations/{id}` - Get reservation by ID
- `GET /api/reservations/guest/{guestId}` - Get guest reservations
- `GET /api/reservations/status/{status}` - Get reservations by status
- `PUT /api/reservations/{id}` - Update reservation
- `PATCH /api/reservations/{id}/cancel` - Cancel reservation
- `PATCH /api/reservations/{id}/check-in` - Check in
- `PATCH /api/reservations/{id}/check-out` - Check out
- `DELETE /api/reservations/{id}` - Delete reservation

### Payment Management

- `POST /api/payments` - Create payment
- `GET /api/payments` - Get all payments
- `GET /api/payments/{id}` - Get payment by ID
- `GET /api/payments/reservation/{reservationId}` - Get payments by reservation
- `GET /api/payments/status/{status}` - Get payments by status
- `PUT /api/payments/{id}` - Update payment
- `PATCH /api/payments/{id}/complete` - Complete payment
- `DELETE /api/payments/{id}` - Delete payment

### Invoice Management

- `GET /api/invoices` - Get all invoices
- `GET /api/invoices/{id}` - Get invoice by ID
- `GET /api/invoices/number/{invoiceNumber}` - Get invoice by number
- `GET /api/invoices/payment/{paymentId}` - Get invoice by payment

### Audit Log Management

- `GET /api/audit-logs` - Get all audit logs
- `GET /api/audit-logs/{id}` - Get audit log by ID
- `GET /api/audit-logs/user/{userId}` - Get logs by user
- `GET /api/audit-logs/entity/{entity}` - Get logs by entity
- `GET /api/audit-logs/entity/{entity}/{entityId}` - Get logs by entity and ID
- `GET /api/audit-logs/status/{status}` - Get logs by status
- `GET /api/audit-logs/date-range` - Get logs by date range

## Configuration

### Environment Variables

The application can be configured using environment variables:

- `SPRING_DATASOURCE_URL` - Database URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `KEYCLOAK_AUTH_SERVER_URL` - Keycloak server URL
- `KEYCLOAK_REALM` - Keycloak realm name
- `KEYCLOAK_RESOURCE` - Keycloak client ID
- `SERVER_PORT` - Application port (default: 8080)

### Application Profiles

The application uses Spring profiles for different environments:

- `application.yml` - Default configuration
- Environment-specific overrides via environment variables

## Development

### Building the Project

```bash
cd hotelmanagement
mvn clean install
```

### Running Tests

```bash
mvn test
```

### Building Docker Image

```bash
docker build -t hotel-backend:latest ./hotelmanagement
```

## API Documentation

Once the application is running, access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON specification:

```
http://localhost:8080/api-docs
```

## Audit Logging

All CRUD operations are automatically logged with:

- User information (if authenticated)
- Action performed
- Entity affected
- IP address
- Timestamp
- Success/failure status

View audit logs through the Audit Log Management API endpoints.

## Database Initialization

The database schema is automatically initialized using SQL scripts in `init-scripts/`:

- `01-init-keycloak-db.sql` - Keycloak database setup
- `02-init-hotel-schema.sql` - Hotel management schema

## Troubleshooting

### Database Connection Issues

- Ensure PostgreSQL container is running: `docker-compose ps`
- Check connection settings in `application.yml`
- Verify network connectivity: `docker-compose logs postgres`

### Keycloak Issues

- Access Keycloak admin console: http://localhost:8180
- Default credentials: admin/admin123
- Check Keycloak logs: `docker-compose logs keycloak`

### Application Logs

```bash
docker-compose logs backend
```

## Future Enhancements

As per project rules, these features will be implemented after the basic system:

- Keycloak Integration for JWT authentication
- Room Service Module
- Email/SMS Notification Service
- API Rate Limiting
- ELK Stack for log analytics
- Grafana dashboard for audit analytics

## License

MIT License

## Support

For issues and questions, please contact the development team or create an issue in the repository.
