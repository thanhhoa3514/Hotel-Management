package com.thanhhoa.hotelmanagement.mapper;

import com.thanhhoa.hotelmanagement.dto.request.GuestRequest;
import com.thanhhoa.hotelmanagement.dto.response.GuestResponse;
import com.thanhhoa.hotelmanagement.entity.Guest;
import org.springframework.stereotype.Component;

/**
 * Mapper for Guest entity - Following Single Responsibility Principle
 */
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
                guest.getKeycloakUserId(),
                guest.getCreatedAt(),
                guest.getUpdatedAt());
    }

    public void updateEntity(Guest guest, GuestRequest request) {
        guest.setFullName(request.fullName());
        guest.setEmail(request.email());
        guest.setPhone(request.phone());
        guest.setAddress(request.address());
        if (request.keycloakUserId() != null) {
            guest.setKeycloakUserId(request.keycloakUserId());
        }
    }
}
