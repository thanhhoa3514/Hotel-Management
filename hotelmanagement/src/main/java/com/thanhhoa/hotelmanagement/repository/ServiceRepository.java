package com.thanhhoa.hotelmanagement.repository;

import com.thanhhoa.hotelmanagement.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findByName(String name);

    boolean existsByName(String name);
}
