# Quick Start Guide

## Running the Hotel Management System

### 1. Start the System

```bash
docker-compose up -d
```

This command will start:

- PostgreSQL database on port 5432
- Keycloak authentication server on port 8180
- Hotel Management backend on port 8080

### 2. Check Services Status

```bash
docker-compose ps
```

Wait until all services show "healthy" status.

### 3. Access the Application

- Swagger UI (API Documentation): http://localhost:8080/swagger-ui.html
- Backend API: http://localhost:8080/api
- Keycloak Admin Console: http://localhost:8180 (admin/admin123)

### 4. Test the APIs

#### Create a User

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "role": "GUEST"
  }'
```

#### Create a Room

```bash
curl -X POST http://localhost:8080/api/rooms \
  -H "Content-Type: application/json" \
  -d '{
    "roomNumber": "101",
    "roomType": "DELUXE",
    "price": 150.00,
    "description": "Deluxe room with ocean view"
  }'
```

#### Get All Available Rooms

```bash
curl http://localhost:8080/api/rooms/available
```

#### Create a Guest Profile

First, note the user ID from the create user response, then:

```bash
curl -X POST http://localhost:8080/api/guests \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "fullName": "John Doe",
    "phone": "+1234567890",
    "address": "123 Main St, City"
  }'
```

#### Create a Reservation

```bash
curl -X POST http://localhost:8080/api/reservations \
  -H "Content-Type: application/json" \
  -d '{
    "guestId": 1,
    "roomId": 1,
    "checkIn": "2025-11-01",
    "checkOut": "2025-11-05"
  }'
```

#### Process Payment

```bash
curl -X POST http://localhost:8080/api/payments \
  -H "Content-Type: application/json" \
  -d '{
    "reservationId": 1,
    "paymentMethod": "CREDIT_CARD",
    "amount": 600.00,
    "status": "COMPLETED"
  }'
```

#### View Audit Logs

```bash
curl http://localhost:8080/api/audit-logs
```

### 5. Explore with Swagger UI

Open http://localhost:8080/swagger-ui.html in your browser to:

- See all available endpoints
- Test APIs interactively
- View request/response schemas
- Check validation rules

### 6. Common Operations

#### Check In a Guest

```bash
curl -X PATCH http://localhost:8080/api/reservations/1/check-in
```

#### Check Out a Guest

```bash
curl -X PATCH http://localhost:8080/api/reservations/1/check-out
```

#### Cancel a Reservation

```bash
curl -X PATCH http://localhost:8080/api/reservations/1/cancel
```

#### View Invoice

```bash
curl http://localhost:8080/api/invoices/payment/1
```

### 7. Stop the System

```bash
docker-compose down
```

To also remove volumes (database data):

```bash
docker-compose down -v
```

## Troubleshooting

### Port Already in Use

If ports 5432, 8080, or 8180 are already in use, modify the ports in `docker-compose.yml`:

```yaml
services:
  postgres:
    ports:
      - "5433:5432" # Change 5432 to 5433
```

### Database Connection Error

Wait for PostgreSQL to fully initialize (about 10-15 seconds) before the backend starts.

### View Logs

```bash
# All services
docker-compose logs

# Specific service
docker-compose logs backend
docker-compose logs postgres
docker-compose logs keycloak
```

### Rebuild After Code Changes

```bash
docker-compose down
docker-compose build
docker-compose up -d
```

## API Response Format

All API responses follow this format:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {},
  "timestamp": "2025-10-30T10:30:00"
}
```

## Room Types

- SINGLE
- DOUBLE
- SUITE
- DELUXE
- PRESIDENTIAL

## Room Status

- AVAILABLE
- OCCUPIED
- MAINTENANCE
- RESERVED

## Reservation Status

- PENDING
- CONFIRMED
- CHECKED_IN
- CHECKED_OUT
- CANCELLED

## Payment Methods

- CASH
- CREDIT_CARD
- DEBIT_CARD
- BANK_TRANSFER
- ONLINE_PAYMENT

## User Roles

- ADMIN (full access)
- STAFF (manage operations)
- GUEST (limited access)

## Next Steps

1. Review the full API documentation at `/swagger-ui.html`
2. Check the comprehensive README.md for detailed information
3. Explore the audit logs to see all tracked operations
4. Test the reservation conflict detection by trying to book the same room for overlapping dates
5. Verify invoice generation after completing a payment
