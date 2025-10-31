package com.thanhhoa.hotelmanagement.dto.request;

import com.thanhhoa.hotelmanagement.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotBlank(message = "Username is required") String username,

        @NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email,

        String password,

        @NotNull(message = "Role is required") UserRole role) {
}
