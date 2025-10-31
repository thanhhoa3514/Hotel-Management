# Hotel Management System - Architecture Documentation

## Overview

This document describes the refactored architecture following SOLID principles and the actual database schema from `docs/db.sql`.

## Key Architectural Changes

### 1. SOLID Principles Implementation

#### Dependency Inversion Principle (DIP)

- All services depend on **interfaces**, not concrete implementations
- Controllers inject `IServiceName` instead of `ServiceName`
- Benefits: Easy testing, loose coupling, flexible implementation swapping

```java
// Before (Violates DIP)
@RestController
public class GuestController {
    private final GuestService guestService; // Concrete dependency
}

// After (Follows DIP)
@RestController
public class GuestController {
    private final IGuestService guestService; // Interface dependency
}
```

#### Single Responsibility Principle (SRP)

- Separate `IRoomImageService` for image management
- `IServiceManagementService` for hotel services (not to confuse with Spring @Service)
- Each service has ONE reason to change

#### Open/Closed Principle (OCP)

- Interfaces allow extension without modification
- Can add new implementations without changing existing code

### 2. Database Schema Changes

#### Lookup Tables Pattern

Instead of enums, we use database tables for better flexibility:

- `room_types` - Instead of enum RoomType
- `room_statuses` - Instead of enum RoomStatus
- `reservation_statuses` - Instead of enum ReservationStatus
- `payment_statuses` - Instead of enum PaymentStatus

Benefits:

- Easy to add new types without code changes
- Can store additional metadata (description, price)
- Database-level referential integrity

#### No Users Table - Keycloak Integration

- Removed `users` table completely
- `guests` and `staff` directly reference Keycloak via `keycloak_user_id UUID`
- Auth fully delegated to Keycloak

#### Many-to-Many Relationships

**Reservation to Rooms** (via `reservation_rooms`):

- One reservation can have multiple rooms
- Supports booking entire floors or room combinations

**Reservation to Staff** (via `reservation_staff`):

- Track which staff members handle each reservation
- Useful for commission tracking

**Reservation to Services** (via `reservation_services`):

- Add hotel services to reservations (room service, laundry, spa)
- Each service has quantity and calculated total_price

#### New Tables

**room_images**:

- Multiple images per room
- `is_primary` flag for main image
- `display_order` for sorting
- Supports image galleries

**services**:

- Hotel services catalog (Room Service, Laundry, Spa, etc.)
- Separate from Spring @Service annotation

**invoice_details**:

- Itemized billing
- `item_type`: 'ROOM' or 'SERVICE'
- Full breakdown of charges

## Entity Relationships

```
RoomType 1---* Room *---* ReservationRoom *---1 Reservation 1---* Guest
RoomStatus 1---* Room
Room 1---* RoomImage

Reservation 1---* ReservationStaff *---1 Staff
Reservation 1---* ReservationService *---1 Service
Reservation *---1 ReservationStatus

Reservation 1---* Payment *---1 PaymentStatus
Payment 1---1 Invoice
Invoice 1---* InvoiceDetail
Invoice *---1 Staff (who issued)
Invoice *---1 Reservation
```

## Service Layer Architecture

### Interface Hierarchy

```
service/
├── interfaces/
│   ├── IGuestService.java
│   ├── IStaffService.java
│   ├── IRoomService.java
│   ├── IRoomImageService.java
│   ├── IRoomTypeService.java
│   ├── IReservationService.java
│   ├── IServiceManagementService.java
│   ├── IPaymentService.java
│   ├── IInvoiceService.java
│   └── IAuditLogService.java
└── impl/
    ├── GuestServiceImpl.java
    ├── StaffServiceImpl.java
    ├── RoomServiceImpl.java
    ├── RoomImageServiceImpl.java
    ├── RoomTypeServiceImpl.java
    ├── ReservationServiceImpl.java
    ├── ServiceManagementServiceImpl.java
    ├── PaymentServiceImpl.java
    ├── InvoiceServiceImpl.java
    └── AuditLogServiceImpl.java
```

### Dependency Injection Pattern

```java
@Service
public class GuestServiceImpl implements IGuestService {

    private final GuestRepository guestRepository;

    // Constructor injection (recommended)
    public GuestServiceImpl(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }
}

@RestController
public class GuestController {

    private final IGuestService guestService; // Depend on interface

    public GuestController(IGuestService guestService) {
        this.guestService = guestService;
    }
}
```

## Mappers - Single Responsibility

Each mapper class handles ONE entity type:

```
mapper/
├── GuestMapper.java
├── StaffMapper.java
├── RoomMapper.java
├── RoomImageMapper.java
├── ReservationMapper.java
├── ServiceMapper.java
├── PaymentMapper.java
└── InvoiceMapper.java
```

## Testing Strategy

With interfaces, testing becomes easier:

```java
@ExtendWith(MockitoExtension.class)
class GuestControllerTest {

    @Mock
    private IGuestService guestService; // Mock the interface

    @InjectMocks
    private GuestController guestController;

    @Test
    void testCreateGuest() {
        // Easy to mock interface behavior
        when(guestService.createGuest(any())).thenReturn(response);
        // Test controller logic
    }
}
```

## Benefits of New Architecture

### 1. Testability

- Easy to mock interfaces
- Unit tests don't need database
- Fast test execution

### 2. Maintainability

- Clear separation of concerns
- Each class has one responsibility
- Easy to locate bugs

### 3. Scalability

- Can swap implementations (e.g., caching layer)
- Add new features without modifying existing code
- Microservices-ready (each service can be extracted)

### 4. Flexibility

- Lookup tables allow runtime configuration
- No code changes for new room types/statuses
- Easy to add new services

## Migration from Old Code

### Old Pattern (Violates SOLID)

```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GuestRepository guestRepository; // Too many dependencies
    @Autowired
    private StaffRepository staffRepository;

    // Mixed responsibilities
    public void createUser() { }
    public void createGuest() { } // Should be in GuestService
    public void createStaff() { } // Should be in StaffService
}
```

### New Pattern (Follows SOLID)

```java
public interface IGuestService {
    GuestResponse createGuest(GuestRequest request);
}

@Service
public class GuestServiceImpl implements IGuestService {
    private final GuestRepository guestRepository; // Single dependency

    @Override
    public GuestResponse createGuest(GuestRequest request) {
        // Single responsibility
    }
}
```

## API Layer

Controllers depend on interfaces:

```java
@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final IGuestService guestService;

    @PostMapping
    public ResponseEntity<ApiResponse<GuestResponse>> createGuest(
            @Valid @RequestBody GuestRequest request) {
        return ResponseEntity.ok(
            ApiResponse.success(guestService.createGuest(request))
        );
    }
}
```

## Configuration

Spring automatically wires interface to implementation:

```java
@Configuration
public class ServiceConfig {
    // No explicit @Bean needed
    // Spring finds IGuestService and autowires GuestServiceImpl
}
```

## Future Enhancements

With this architecture, easy to add:

1. **Caching Layer**

```java
@Service
@Primary
public class CachedGuestService implements IGuestService {
    private final IGuestService delegate;
    private final CacheManager cacheManager;
    // Wrap calls with cache logic
}
```

2. **Event Publishing**

```java
@Service
public class EventPublishingGuestService implements IGuestService {
    private final IGuestService delegate;
    private final ApplicationEventPublisher eventPublisher;
    // Publish events after operations
}
```

3. **Microservices**

- Each service interface can become a separate microservice
- Use Feign clients to implement interfaces
- No controller changes needed

## Conclusion

This architecture provides:

- Loose coupling via interfaces
- High cohesion via single responsibility
- Easy testing with mocks
- Flexible and extensible design
- Production-ready structure

All following **SOLID principles** and **best practices** for enterprise Java applications.
