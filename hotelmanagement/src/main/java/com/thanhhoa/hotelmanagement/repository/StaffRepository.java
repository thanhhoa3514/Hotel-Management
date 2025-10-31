package com.thanhhoa.hotelmanagement.repository;

import java.util.UUID;

import com.thanhhoa.hotelmanagement.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {
    Optional<Staff> findByEmail(String email);

    Optional<Staff> findByKeycloakUserId(UUID keycloakUserId);

    List<Staff> findByPosition(String position);

    boolean existsByEmail(String email);

    boolean existsByKeycloakUserId(UUID keycloakUserId);

    void deleteByEmail(String email);

    Optional<Staff> findById(UUID id);
}
