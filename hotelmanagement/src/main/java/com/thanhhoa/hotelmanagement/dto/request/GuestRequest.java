package com.thanhhoa.hotelmanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record GuestRequest(
        @NotBlank(message = "Full name is required") String fullName,

        @NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email,

        String phone,
        String address,
        UUID keycloakUserId) {
}
