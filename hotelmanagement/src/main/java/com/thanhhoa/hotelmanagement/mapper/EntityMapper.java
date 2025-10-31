package com.thanhhoa.hotelmanagement.mapper;

import com.thanhhoa.hotelmanagement.dto.request.*;
import com.thanhhoa.hotelmanagement.dto.response.*;
import com.thanhhoa.hotelmanagement.entity.*;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    // User mappings
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
                guest.getKeycloakUserId(),
                guest.getCreatedAt(),
                guest.getUpdatedAt());
    }

    // Staff mappings
    public Staff toEntity(StaffRequest request) {
        return Staff.builder()
                .keycloakUserId(request.keycloakUserId())
                .fullName(request.fullName())
                .position(request.position())
                .build();
    }

    public StaffResponse toResponse(Staff staff) {
        return new StaffResponse(
                staff.getKeycloakUserId(),
                staff.getFullName(),
                staff.getPosition(),

                staff.getCreatedAt(),
                staff.getUpdatedAt());
    }

    // Room mappings
    public Room toEntity(RoomRequest request) {
        return Room.builder()
                .roomNumber(request.roomNumber())
                .roomType(RoomType.builder()
                        .id(request.roomTypeId())
                        .build())
                .roomStatus(request.roomStatus() != null ? request.roomStatus() : RoomStatus.AVAILABLE)
                .floor(request.floor())
                .note(request.note())
                .build();
    }

    public ServiceResponse toResponse(Service service) {
        return new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getPrice());
    }

    public RoomResponse toResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                new RoomTypeResponse(room.getRoomType().getId(), room.getRoomType().getName(),
                        room.getRoomType().getDescription(), room.getRoomType().getPricePerNight()),
                room.getRoomStatus(),
                room.getFloor(),
                room.getNote(),
                room.getImages().stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList()));
    }

    public RoomImageResponse toResponse(RoomImage image) {
        return new RoomImageResponse(
                image.getId(),
                image.getRoom().getId(),
                image.getImageUrl(),
                image.getDescription(),
                image.getIsPrimary(),
                image.getDisplayOrder(),
                image.getCreatedAt());
    }

    // Reservation mappings
    public ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                new GuestResponse(reservation.getGuest().getId(), reservation.getGuest().getFullName(),
                        reservation.getGuest().getKeycloakUserId(), reservation.getGuest().getCreatedAt(),
                        reservation.getGuest().getUpdatedAt()),
                reservation.getReservationRooms().stream()
                        .map(room -> new RoomResponse(room.getRoom().getId(), room.getRoom().getRoomNumber(),
                                new RoomTypeResponse(room.getRoom().getRoomType().getId(),
                                        room.getRoom().getRoomType().getName(),
                                        room.getRoom().getRoomType().getDescription(),
                                        room.getRoom().getRoomType().getPricePerNight()),
                                room.getRoom().getRoomStatus(), room.getRoom().getFloor(), room.getRoom().getNote(),
                                room.getRoom().getImages().stream()
                                        .map(image -> new RoomImageResponse(image.getId(), image.getRoom().getId(),
                                                image.getImageUrl(), image.getDescription(), image.getIsPrimary(),
                                                image.getDisplayOrder(), image.getCreatedAt()))
                                        .collect(Collectors.toList())))
                        .collect(Collectors.toList()),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getTotalAmount(),
                reservation.getStatus(),
                reservation.getReservationServices().stream()
                        .map(service -> new ServiceResponse(service.getId(), service.getService().getName(),
                                service.getService().getDescription(), service.getService().getPrice()))
                        .collect(Collectors.toList()),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt());
    }

    // Payment mappings
    public Payment toEntity(PaymentRequest request, Reservation reservation) {
        return Payment.builder()
                .reservation(reservation)
                .method(request.method())
                .amount(request.amount())
                .status(request.status() != null ? request.status() : PaymentStatus.PENDING)
                .build();
    }

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getReservation().getId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getTransactionCode(),
                payment.getPaymentDate());
    }

    // Invoice mappings
    public InvoiceResponse toResponse(Invoice invoice) {
        return new InvoiceResponse(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getReservation().getId(),
                invoice.getPayment().getId(),
                invoice.getStaff().getKeycloakUserId(),
                invoice.getIssueDate(),
                invoice.getTotalAmount(),
                invoice.getTax(),
                invoice.getDiscount(),
                invoice.getFinalAmount(),
                invoice.getInvoiceDetails().stream()
                        .map(detail -> new InvoiceDetailResponse(detail.getId(), detail.getItemType(),
                                detail.getItemId(), detail.getDescription(), detail.getQuantity(),
                                detail.getUnitPrice(), detail.getTotalPrice()))
                        .collect(Collectors.toList()),
                invoice.getCreatedAt());
    }

    // AuditLog mappings
    public AuditLogResponse toResponse(AuditLog auditLog) {
        return new AuditLogResponse(
                auditLog.getId(),
                auditLog.getUserId(),
                auditLog.getUserRole(),
                auditLog.getAction(),
                auditLog.getEntity(),
                auditLog.getEntityId(),
                auditLog.getDescription(),
                auditLog.getStatus(),
                auditLog.getIpAddress(),
                auditLog.getTimestamp());
    }
}
