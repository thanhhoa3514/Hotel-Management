package com.thanhhoa.hotelmanagement.repository;

import com.thanhhoa.hotelmanagement.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByEmail(String email);

    Optional<Guest> findByKeycloakUserId(UUID keycloakUserId);

    boolean existsByEmail(String email);

    boolean existsByKeycloakUserId(UUID keycloakUserId);
}
