package com.thanhhoa.hotelmanagement.service;

import com.thanhhoa.hotelmanagement.audit.Auditable;
import com.thanhhoa.hotelmanagement.dto.request.GuestRequest;
import com.thanhhoa.hotelmanagement.dto.response.GuestResponse;
import com.thanhhoa.hotelmanagement.entity.Guest;
import com.thanhhoa.hotelmanagement.entity.User;
import com.thanhhoa.hotelmanagement.exception.DuplicateResourceException;
import com.thanhhoa.hotelmanagement.exception.ResourceNotFoundException;
import com.thanhhoa.hotelmanagement.mapper.EntityMapper;
import com.thanhhoa.hotelmanagement.repository.GuestRepository;
import com.thanhhoa.hotelmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GuestService {

    private final GuestRepository guestRepository;
    private final UserRepository userRepository;
    private final EntityMapper mapper;

    @Auditable(action = "CREATE", entity = "GUEST")
    public GuestResponse createGuest(GuestRequest request) {
        log.debug("Creating guest for user ID: {}", request.keycloakUserId());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", "keycloakUserId", request.keycloakUserId()));

        if (guestRepository.existsByKeycloakUserId(request.keycloakUserId())) {
            throw new DuplicateResourceException("Guest", "keycloakUserId", request.keycloakUserId());
        }

        Guest guest = mapper.toEntity(request);
        Guest savedGuest = guestRepository.save(guest);

        log.info("Guest created successfully with ID: {}", savedGuest.getId());
        return mapper.toResponse(savedGuest);
    }

    @Transactional(readOnly = true)
    public GuestResponse getGuestById(Long id) {
        log.debug("Fetching guest with ID: {}", id);
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", id));
        return mapper.toResponse(guest);
    }

    @Transactional(readOnly = true)
    public GuestResponse getGuestByUserId(UUID userId) {
        log.debug("Fetching guest with user ID: {}", userId);
        Guest guest = guestRepository.findByKeycloakUserId(UUID.fromString(userId.toString()))
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "userId", userId));
        return mapper.toResponse(guest);
    }

    @Transactional(readOnly = true)
    public List<GuestResponse> getAllGuests() {
        log.debug("Fetching all guests");
        return guestRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Auditable(action = "UPDATE", entity = "GUEST")
    public GuestResponse updateGuest(Long id, GuestRequest request) {
        log.debug("Updating guest with ID: {}", id);

        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", id));

        guest.setFullName(request.fullName());
        guest.setPhone(request.phone());
        guest.setAddress(request.address());

        Guest updatedGuest = guestRepository.save(guest);
        log.info("Guest updated successfully with ID: {}", updatedGuest.getId());
        return mapper.toResponse(updatedGuest);
    }

    @Auditable(action = "DELETE", entity = "GUEST")
    public void deleteGuest(Long id) {
        log.debug("Deleting guest with ID: {}", id);

        if (!guestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Guest", "id", id);
        }

        guestRepository.deleteById(id);
        log.info("Guest deleted successfully with ID: {}", id);
    }
}
