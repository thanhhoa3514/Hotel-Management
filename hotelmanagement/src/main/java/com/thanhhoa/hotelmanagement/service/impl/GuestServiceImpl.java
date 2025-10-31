package com.thanhhoa.hotelmanagement.service.impl;

import com.thanhhoa.hotelmanagement.audit.Auditable;
import com.thanhhoa.hotelmanagement.dto.request.GuestRequest;
import com.thanhhoa.hotelmanagement.dto.response.GuestResponse;
import com.thanhhoa.hotelmanagement.entity.Guest;
import com.thanhhoa.hotelmanagement.exception.DuplicateResourceException;
import com.thanhhoa.hotelmanagement.exception.ResourceNotFoundException;
import com.thanhhoa.hotelmanagement.mapper.GuestMapper;
import com.thanhhoa.hotelmanagement.repository.GuestRepository;
import com.thanhhoa.hotelmanagement.service.interfaces.IGuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of IGuestService - Following SOLID principles
 * - Dependency Inversion: Depends on IGuestService interface
 * - Single Responsibility: Only handles guest-related business logic
 * - Open/Closed: Can be extended without modification
 */
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

        // Check for duplicate email
        if (guestRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Guest", "email", request.email());
        }

        // Check for duplicate Keycloak user ID if provided
        if (request.keycloakUserId() != null &&
                guestRepository.existsByKeycloakUserId(request.keycloakUserId())) {
            throw new DuplicateResourceException("Guest", "keycloakUserId", request.keycloakUserId());
        }

        Guest guest = guestMapper.toEntity(request);
        Guest savedGuest = guestRepository.save(guest);

        log.info("Guest created successfully with ID: {}", savedGuest.getId());
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
        log.debug("Fetching guest with email: {}", email);
        Guest guest = guestRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "email", email));
        return guestMapper.toResponse(guest);
    }

    @Override
    @Transactional(readOnly = true)
    public GuestResponse getGuestByKeycloakUserId(UUID keycloakUserId) {
        log.debug("Fetching guest with Keycloak user ID: {}", keycloakUserId);
        Guest guest = guestRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "keycloakUserId", keycloakUserId));
        return guestMapper.toResponse(guest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuestResponse> getAllGuests() {
        log.debug("Fetching all guests");
        return guestRepository.findAll().stream()
                .map(guestMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Auditable(action = "UPDATE", entity = "GUEST")
    public GuestResponse updateGuest(Long id, GuestRequest request) {
        log.debug("Updating guest with ID: {}", id);

        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", id));

        // Check for duplicate email (if changed)
        if (!guest.getEmail().equals(request.email()) &&
                guestRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Guest", "email", request.email());
        }

        guestMapper.updateEntity(guest, request);
        Guest updatedGuest = guestRepository.save(guest);

        log.info("Guest updated successfully with ID: {}", updatedGuest.getId());
        return guestMapper.toResponse(updatedGuest);
    }

    @Override
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
