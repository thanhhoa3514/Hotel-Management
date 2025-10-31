package com.thanhhoa.hotelmanagement.service.interfaces;

import com.thanhhoa.hotelmanagement.dto.request.GuestRequest;
import com.thanhhoa.hotelmanagement.dto.response.GuestResponse;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Guest management
 * Following Dependency Inversion Principle - depend on abstraction, not
 * concrete implementation
 */
public interface IGuestService {

    GuestResponse createGuest(GuestRequest request);

    GuestResponse getGuestById(Long id);

    GuestResponse getGuestByEmail(String email);

    GuestResponse getGuestByKeycloakUserId(UUID keycloakUserId);

    List<GuestResponse> getAllGuests();

    GuestResponse updateGuest(Long id, GuestRequest request);

    void deleteGuest(Long id);
}
