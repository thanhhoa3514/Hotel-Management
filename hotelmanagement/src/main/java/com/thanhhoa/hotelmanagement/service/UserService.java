package com.thanhhoa.hotelmanagement.service;

import com.thanhhoa.hotelmanagement.audit.Auditable;
import com.thanhhoa.hotelmanagement.dto.request.UserRequest;
import com.thanhhoa.hotelmanagement.dto.response.UserResponse;
import com.thanhhoa.hotelmanagement.entity.Guest;
import com.thanhhoa.hotelmanagement.entity.User;
import com.thanhhoa.hotelmanagement.entity.UserRole;
import com.thanhhoa.hotelmanagement.exception.DuplicateResourceException;
import com.thanhhoa.hotelmanagement.exception.ResourceNotFoundException;
import com.thanhhoa.hotelmanagement.mapper.EntityMapper;
import com.thanhhoa.hotelmanagement.mapper.GuestMapper;
import com.thanhhoa.hotelmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final GuestMapper guestMapper;

    // @Auditable(action = "CREATE", entity = "USER")
    // public UserResponse createUser(UserRequest request) {
    // log.debug("Creating user with username: {}", request.username());

    // if (userRepository.existsByUsername(request.username())) {
    // throw new DuplicateResourceException("User", "username", request.username());
    // }

    // if (userRepository.existsByEmail(request.email())) {
    // throw new DuplicateResourceException("User", "email", request.email());
    // }

    // User user = mapper.toEntity(request);
    // User savedUser = userRepository.save(user);

    // log.info("User created successfully with ID: {}", savedUser.getId());
    // return mapper.toResponse(savedUser);
    // }

    // @Transactional(readOnly = true)
    // public UserResponse getUserById(Long id) {
    // log.debug("Fetching user with ID: {}", id);
    // User user = userRepository.findById(id)
    // .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    // return guestMapper.toResponse(user);
    // }

    // @Transactional(readOnly = true)
    // public UserResponse getUserByUsername(String username) {
    // log.debug("Fetching user with username: {}", username);
    // User user = userRepository.findByUsername(username)
    // .orElseThrow(() -> new ResourceNotFoundException("User", "username",
    // username));
    // return guestMapper.toResponse(user);
    // }

    // @Transactional(readOnly = true)
    // public List<UserResponse> getAllUsers() {
    // log.debug("Fetching all users");
    // return userRepository.findAll().stream()
    // .map(guestMapper::toResponse)
    // .collect(Collectors.toList());
    // }

    // @Transactional(readOnly = true)
    // public List<UserResponse> getUsersByRole(UserRole role) {
    // log.debug("Fetching users with role: {}", role);
    // return userRepository.findByRole(role).stream()
    // .map(guestMapper::toResponse)
    // .collect(Collectors.toList());
    // }

    // @Auditable(action = "UPDATE", entity = "USER")
    // public UserResponse updateUser(Long id, UserRequest request) {
    // log.debug("Updating user with ID: {}", id);

    // User user = userRepository.findById(id)
    // .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

    // if (!user.getUsername().equals(request.username()) &&
    // userRepository.existsByUsername(request.username())) {
    // throw new DuplicateResourceException("User", "username", request.username());
    // }

    // if (!user.getEmail().equals(request.email()) &&
    // userRepository.existsByEmail(request.email())) {
    // throw new DuplicateResourceException("User", "email", request.email());
    // }

    // user.setUsername(request.username());
    // user.setEmail(request.email());
    // if (request.password() != null && !request.password().isEmpty()) {
    // user.setPassword(request.password());
    // }
    // user.setRole(request.role());

    // Guest updatedGuest = guestMapper.toEntity(request);
    // log.info("Guest updated successfully with ID: {}", updatedGuest.getId());
    // return guestMapper.toResponse(updatedGuest);
    // }

    // @Auditable(action = "DELETE", entity = "USER")
    // public void deleteUser(Long id) {
    // log.debug("Deleting user with ID: {}", id);

    // if (!userRepository.existsById(id)) {
    // throw new ResourceNotFoundException("User", "id", id);
    // }

    // userRepository.deleteById(id);
    // log.info("User deleted successfully with ID: {}", id);
    // }
}
