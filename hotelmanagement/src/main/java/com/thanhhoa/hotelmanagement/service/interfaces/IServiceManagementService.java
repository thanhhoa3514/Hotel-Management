package com.thanhhoa.hotelmanagement.service.interfaces;

import com.thanhhoa.hotelmanagement.dto.request.ServiceRequest;
import com.thanhhoa.hotelmanagement.dto.response.ServiceResponse;

import java.util.List;

/**
 * Service interface for Hotel Services management (Room Service, Laundry, etc.)
 * Named IServiceManagementService to avoid confusion with Spring's @Service
 * annotation
 */
public interface IServiceManagementService {

    ServiceResponse createService(ServiceRequest request);

    ServiceResponse getServiceById(Long id);

    ServiceResponse getServiceByName(String name);

    List<ServiceResponse> getAllServices();

    ServiceResponse updateService(Long id, ServiceRequest request);

    void deleteService(Long id);
}
