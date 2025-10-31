package com.thanhhoa.hotelmanagement.service;

import com.thanhhoa.hotelmanagement.audit.Auditable;
import com.thanhhoa.hotelmanagement.dto.request.StaffRequest;
import com.thanhhoa.hotelmanagement.dto.response.StaffResponse;
import com.thanhhoa.hotelmanagement.entity.Staff;
import com.thanhhoa.hotelmanagement.entity.User;
import com.thanhhoa.hotelmanagement.exception.DuplicateResourceException;
import com.thanhhoa.hotelmanagement.exception.ResourceNotFoundException;
import com.thanhhoa.hotelmanagement.mapper.EntityMapper;
import com.thanhhoa.hotelmanagement.repository.StaffRepository;
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
public class StaffService {

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final EntityMapper mapper;

    @Auditable(action = "CREATE", entity = "STAFF")
    public StaffResponse createStaff(StaffRequest request) {
        log.debug("Creating staff for user ID: {}", request.keycloakUserId());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.email()));

        if (staffRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Staff", "email", request.email());
        }

        Staff staff = mapper.toEntity(request);
        Staff savedStaff = staffRepository.save(staff);

        log.info("Staff created successfully with ID: {}", savedStaff.getId());
        return mapper.toResponse(savedStaff);
    }

    @Transactional(readOnly = true)
    public StaffResponse getStaffById(UUID id) {
        log.debug("Fetching staff with ID: {}", id);
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "keycloakUserId", id));
        return mapper.toResponse(staff);
    }

    @Transactional(readOnly = true)
    public StaffResponse getStaffByEmail(String email) {
        log.debug("Fetching staff with email: {}", email);
        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "email", email));
        return mapper.toResponse(staff);
    }

    @Transactional(readOnly = true)
    public List<StaffResponse> getAllStaff() {
        log.debug("Fetching all staff");
        return staffRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StaffResponse> getStaffByPosition(String position) {
        log.debug("Fetching staff in position: {}", position);
        return staffRepository.findByPosition(position).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Auditable(action = "UPDATE", entity = "STAFF")
    public StaffResponse updateStaff(String email, StaffRequest request) {
        log.debug("Updating staff with email: {}", email);

        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "email", email));

        staff.setFullName(request.fullName());
        staff.setPosition(request.position());

        Staff updatedStaff = staffRepository.save(staff);
        log.info("Staff updated successfully with ID: {}", updatedStaff.getId());
        return mapper.toResponse(updatedStaff);
    }

    @Auditable(action = "DELETE", entity = "STAFF")
    public void deleteStaff(String email) {
        log.debug("Deleting staff with email: {}", email);

        if (!staffRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException("Staff", "email", email);
        }

        staffRepository.deleteByEmail(email);
        log.info("Staff deleted successfully with email: {}", email);
    }
}
