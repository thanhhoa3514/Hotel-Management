package com.thanhhoa.hotelmanagement.service.interfaces;

import com.thanhhoa.hotelmanagement.dto.request.StaffRequest;
import com.thanhhoa.hotelmanagement.dto.response.StaffResponse;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Staff management
 */
public interface IStaffService {

    StaffResponse createStaff(StaffRequest request);

    StaffResponse getStaffById(Long id);

    StaffResponse getStaffByEmail(String email);

    StaffResponse getStaffByKeycloakUserId(UUID keycloakUserId);

    List<StaffResponse> getAllStaff();

    List<StaffResponse> getStaffByPosition(String position);

    StaffResponse updateStaff(Long id, StaffRequest request);

    void deleteStaff(Long id);
}
