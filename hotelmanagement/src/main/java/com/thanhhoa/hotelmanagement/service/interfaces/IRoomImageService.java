package com.thanhhoa.hotelmanagement.service.interfaces;

import com.thanhhoa.hotelmanagement.dto.request.RoomImageRequest;
import com.thanhhoa.hotelmanagement.dto.response.RoomImageResponse;

import java.util.List;

/**
 * Service interface for Room Image management
 * Following Single Responsibility Principle - separate concern for image
 * management
 */
public interface IRoomImageService {

    RoomImageResponse addImage(RoomImageRequest request);

    RoomImageResponse getImageById(Long id);

    List<RoomImageResponse> getImagesByRoomId(Long roomId);

    RoomImageResponse setPrimaryImage(Long imageId);

    RoomImageResponse updateImage(Long id, RoomImageRequest request);

    void deleteImage(Long id);
}
