# Implementation Guide - Còn lại

## ✅ Đã hoàn thành:

1. Database schema với lookup tables + room_images
2. 17 Entities mới
3. 10 Service interfaces (SOLID)
4. 12 Repositories
5. All DTOs (Request + Response)

## 📝 Cần làm tiếp:

### 1. Mappers (Single Responsibility Principle)

Tạo từng mapper class riêng biệt thay vì 1 EntityMapper lớn:

**File: `hotelmanagement/src/main/java/com/thanhhoa/hotelmanagement/mapper/GuestMapper.java`**

```java
@Component
public class GuestMapper {

    public Guest toEntity(GuestRequest request) {
        return Guest.builder()
                .fullName(request.fullName())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .keycloakUserId(request.keycloakUserId())
                .build();
    }

    public GuestResponse toResponse(Guest guest) {
        return new GuestResponse(
                guest.getId(),
                guest.getFullName(),
                guest.getEmail(),
                guest.getPhone(),
                guest.getAddress(),
                guest.getKeycloakUserId(),
                guest.getCreatedAt(),
                guest.getUpdatedAt()
        );
    }

    public void updateEntity(Guest guest, GuestRequest request) {
        guest.setFullName(request.fullName());
        guest.setEmail(request.email());
        guest.setPhone(request.phone());
        guest.setAddress(request.address());
    }
}
```

Tương tự tạo:

- `StaffMapper.java`
- `RoomTypeMapper.java`
- `RoomMapper.java` (include mapping RoomType)
- `RoomImageMapper.java`
- `ServiceMapper.java`
- `ReservationMapper.java` (complex - map rooms, services)
- `PaymentMapper.java`
- `InvoiceMapper.java`
- `AuditLogMapper.java`

### 2. Service Implementations (Follow SOLID)

**Pattern:** Mỗi service implement interface của nó

**File: `hotelmanagement/src/main/java/com/thanhhoa/hotelmanagement/service/impl/GuestServiceImpl.java`**

```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GuestServiceImpl implements IGuestService {

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    @Override
    @Auditable(action = "CREATE", entity = "GUEST")
    public GuestResponse createGuest(GuestRequest request) {
        log.debug("Creating guest with email: {}", request.email());

        if (guestRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Guest", "email", request.email());
        }

        Guest guest = guestMapper.toEntity(request);
        Guest savedGuest = guestRepository.save(guest);

        log.info("Guest created with ID: {}", savedGuest.getId());
        return guestMapper.toResponse(savedGuest);
    }

    @Override
    @Transactional(readOnly = true)
    public GuestResponse getGuestById(Long id) {
        log.debug("Fetching guest with ID: {}", id);
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", id));
        return guestMapper.toResponse(guest);
    }

    @Override
    @Transactional(readOnly = true)
    public GuestResponse getGuestByEmail(String email) {
        Guest guest = guestRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "email", email));
        return guestMapper.toResponse(guest);
    }

    @Override
    @Transactional(readOnly = true)
    public GuestResponse getGuestByKeycloakUserId(UUID keycloakUserId) {
        Guest guest = guestRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "keycloakUserId", keycloakUserId));
        return guestMapper.toResponse(guest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuestResponse> getAllGuests() {
        return guestRepository.findAll().stream()
                .map(guestMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Auditable(action = "UPDATE", entity = "GUEST")
    public GuestResponse updateGuest(Long id, GuestRequest request) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", id));

        if (!guest.getEmail().equals(request.email()) &&
            guestRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Guest", "email", request.email());
        }

        guestMapper.updateEntity(guest, request);
        Guest updatedGuest = guestRepository.save(guest);

        return guestMapper.toResponse(updatedGuest);
    }

    @Override
    @Auditable(action = "DELETE", entity = "GUEST")
    public void deleteGuest(Long id) {
        if (!guestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Guest", "id", id);
        }
        guestRepository.deleteById(id);
    }
}
```

Tạo tương tự cho:

- `StaffServiceImpl.java`
- `RoomTypeServiceImpl.java`
- `RoomServiceImpl.java`
- `RoomImageServiceImpl.java`
- `ServiceManagementServiceImpl.java`
- `ReservationServiceImpl.java` (complex - handle rooms, services, calculate total)
- `PaymentServiceImpl.java` (auto-generate invoice)
- `InvoiceServiceImpl.java`
- `AuditLogServiceImpl.java` (read-only)

### 3. Update Controllers (Inject Interfaces)

**Pattern:** Controllers depend on interfaces, not implementations

**File: `hotelmanagement/src/main/java/com/thanhhoa/hotelmanagement/controller/GuestController.java`**

```java
@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
@Tag(name = "Guest Management")
public class GuestController {

    private final IGuestService guestService; // Inject interface

    @PostMapping
    @Operation(summary = "Create a new guest")
    public ResponseEntity<ApiResponse<GuestResponse>> createGuest(
            @Valid @RequestBody GuestRequest request) {
        GuestResponse response = guestService.createGuest(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Guest created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GuestResponse>> getGuestById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(guestService.getGuestById(id)));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<GuestResponse>> getGuestByEmail(@PathVariable String email) {
        return ResponseEntity.ok(ApiResponse.success(guestService.getGuestByEmail(email)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GuestResponse>>> getAllGuests() {
        return ResponseEntity.ok(ApiResponse.success(guestService.getAllGuests()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GuestResponse>> updateGuest(
            @PathVariable Long id,
            @Valid @RequestBody GuestRequest request) {
        return ResponseEntity.ok(
            ApiResponse.success("Guest updated", guestService.updateGuest(id, request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
        return ResponseEntity.ok(ApiResponse.success("Guest deleted", null));
    }
}
```

Update/Create controllers:

- `GuestController.java` ✅
- `StaffController.java`
- `RoomTypeController.java` (NEW)
- `RoomController.java`
- `RoomImageController.java` (NEW)
- `ServiceController.java` (hotel services)
- `ReservationController.java`
- `PaymentController.java`
- `InvoiceController.java`
- `AuditLogController.java`

### 4. Complex Service: ReservationServiceImpl

**Key Points:**

- Check room availability
- Create ReservationRoom associations
- Calculate total amount
- Handle reservation status changes

```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final ReservationRoomRepository reservationRoomRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public ReservationResponse createReservation(ReservationRequest request) {
        // 1. Validate guest exists
        Guest guest = guestRepository.findById(request.guestId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", request.guestId()));

        // 2. Validate check-in/out dates
        if (request.checkIn().isAfter(request.checkOut())) {
            throw new BusinessException("Check-in must be before check-out");
        }

        // 3. Validate rooms availability
        for (Long roomId : request.roomIds()) {
            List<Reservation> conflicts = reservationRepository
                    .findConflictingReservations(roomId, request.checkIn(), request.checkOut());
            if (!conflicts.isEmpty()) {
                throw new BusinessException("Room " + roomId + " not available for selected dates");
            }
        }

        // 4. Create reservation
        Reservation reservation = Reservation.builder()
                .guest(guest)
                .checkIn(request.checkIn())
                .checkOut(request.checkOut())
                .status(request.status() != null ? request.status() : ReservationStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        // 5. Add rooms to reservation
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Long roomId : request.roomIds()) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));

            ReservationRoom reservationRoom = ReservationRoom.builder()
                    .reservation(savedReservation)
                    .room(room)
                    .build();
            reservationRoomRepository.save(reservationRoom);

            // Calculate price: room type price * number of nights
            long nights = ChronoUnit.DAYS.between(request.checkIn(), request.checkOut());
            BigDecimal roomCost = room.getRoomType().getPricePerNight()
                    .multiply(BigDecimal.valueOf(nights));
            totalAmount = totalAmount.add(roomCost);
        }

        // 6. Update total amount
        savedReservation.setTotalAmount(totalAmount);
        reservationRepository.save(savedReservation);

        return reservationMapper.toResponse(savedReservation);
    }

    // ... other methods
}
```

### 5. Update AuditAspect

Update để work với UUID thay vì User entity:

```java
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void auditSuccess(JoinPoint joinPoint, Auditable auditable, Object result) {
        createAuditLog(auditable, "SUCCESS", "Operation completed successfully", result);
    }

    @AfterThrowing(pointcut = "@annotation(auditable)", throwing = "exception")
    public void auditFailure(JoinPoint joinPoint, Auditable auditable, Exception exception) {
        createAuditLog(auditable, "FAILURE", "Operation failed: " + exception.getMessage(), null);
    }

    private void createAuditLog(Auditable auditable, String status, String description, Object result) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UUID userId = null;
            String userRole = "ANONYMOUS";

            if (authentication != null && authentication.isAuthenticated()) {
                // Extract UUID from Keycloak JWT
                Object principal = authentication.getPrincipal();
                if (principal instanceof Jwt jwt) {
                    userId = UUID.fromString(jwt.getSubject());
                    userRole = jwt.getClaimAsString("realm_access");
                }
            }

            String ipAddress = getClientIpAddress();
            Long entityId = extractEntityId(result);

            AuditLog auditLog = AuditLog.builder()
                    .userId(userId)
                    .userRole(userRole)
                    .action(auditable.action())
                    .entity(auditable.entity())
                    .entityId(entityId)
                    .description(description)
                    .status(status)
                    .ipAddress(ipAddress)
                    .build();

            auditLogRepository.save(auditLog);

        } catch (Exception e) {
            log.error("Failed to create audit log", e);
        }
    }

    private String getClientIpAddress() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return "unknown";

        HttpServletRequest request = attributes.getRequest();
        String xForwardedFor = request.getHeader("X-Forwarded-For");

        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    private Long extractEntityId(Object result) {
        if (result == null) return null;

        try {
            if (result.getClass().getMethod("id") != null) {
                Object id = result.getClass().getMethod("id").invoke(result);
                return id instanceof Long ? (Long) id : null;
            }
        } catch (Exception e) {
            log.debug("Could not extract entity ID", e);
        }

        return null;
    }
}
```

### 6. Xóa Code Cũ

Delete các files không dùng nữa:

- `User.java` entity (đã có Guest/Staff với Keycloak)
- `UserRole.java` enum (nếu không dùng)
- `UserService.java` (old)
- `UserController.java` (old)
- `UserRepository.java` (old)
- `UserRequest.java`, `UserResponse.java` DTOs (old)
- Old `EntityMapper.java` (replace bằng individual mappers)

### 7. Testing Pattern

**Unit Test với Mockito:**

```java
@ExtendWith(MockitoExtension.class)
class GuestServiceImplTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private GuestMapper guestMapper;

    @InjectMocks
    private GuestServiceImpl guestService;

    @Test
    void createGuest_Success() {
        // Arrange
        GuestRequest request = new GuestRequest(
            "John Doe", "john@example.com", "123456", "Address", null
        );
        Guest guest = new Guest();
        GuestResponse expected = new GuestResponse(1L, "John Doe", "john@example.com",
            "123456", "Address", null, null, null);

        when(guestRepository.existsByEmail(anyString())).thenReturn(false);
        when(guestMapper.toEntity(request)).thenReturn(guest);
        when(guestRepository.save(guest)).thenReturn(guest);
        when(guestMapper.toResponse(guest)).thenReturn(expected);

        // Act
        GuestResponse result = guestService.createGuest(request);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.fullName());
        verify(guestRepository).save(guest);
    }
}
```

## 🎯 Checklist

- [ ] Create 10 Mapper classes (Single Responsibility)
- [ ] Create 10 Service implementation classes
- [ ] Update/Create 10 Controller classes
- [ ] Update AuditAspect for UUID
- [ ] Delete old User-related files
- [ ] Write unit tests for services
- [ ] Update SecurityConfig for Keycloak JWT
- [ ] Test all endpoints with Swagger
- [ ] Update README.md với API mới
- [ ] Update database init scripts

## 🔥 Quick Commands

```bash
# Build project
mvn clean install

# Run locally
mvn spring-boot:run

# Run with Docker
docker-compose up --build

# Access Swagger
http://localhost:8080/swagger-ui.html
```

## 📚 Useful Resources

- SOLID Principles: https://www.baeldung.com/solid-principles
- Spring Dependency Injection: https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-dependencies
- Keycloak Spring Boot: https://www.keycloak.org/docs/latest/securing_apps/
