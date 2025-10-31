package com.thanhhoa.hotelmanagement.controller;

import com.thanhhoa.hotelmanagement.dto.request.UserRequest;
import com.thanhhoa.hotelmanagement.dto.response.ApiResponse;
import com.thanhhoa.hotelmanagement.dto.response.UserResponse;
import com.thanhhoa.hotelmanagement.entity.UserRole;
import com.thanhhoa.hotelmanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    // private final UserService userService;

    // @PostMapping
    // @Operation(summary = "Create a new user")
    // public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid
    // @RequestBody UserRequest request) {
    // UserResponse response = userService.createUser(request);
    // return ResponseEntity
    // .status(HttpStatus.CREATED)
    // .body(ApiResponse.success("User created successfully", response));
    // }

    // @GetMapping("/{id}")
    // @Operation(summary = "Get user by ID")
    // public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable
    // Long id) {
    // UserResponse response = userService.getUserById(id);
    // return ResponseEntity.ok(ApiResponse.success(response));
    // }

    // @GetMapping("/username/{username}")
    // @Operation(summary = "Get user by username")
    // public ResponseEntity<ApiResponse<UserResponse>>
    // getUserByUsername(@PathVariable String username) {
    // UserResponse response = userService.getUserByUsername(username);
    // return ResponseEntity.ok(ApiResponse.success(response));
    // }

    // @GetMapping
    // @Operation(summary = "Get all users")
    // public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
    // List<UserResponse> response = userService.getAllUsers();
    // return ResponseEntity.ok(ApiResponse.success(response));
    // }

    // @GetMapping("/role/{role}")
    // @Operation(summary = "Get users by role")
    // public ResponseEntity<ApiResponse<List<UserResponse>>>
    // getUsersByRole(@PathVariable UserRole role) {
    // List<UserResponse> response = userService.getUsersByRole(role);
    // return ResponseEntity.ok(ApiResponse.success(response));
    // }

    // @PutMapping("/{id}")
    // @Operation(summary = "Update user")
    // public ResponseEntity<ApiResponse<UserResponse>> updateUser(
    // @PathVariable Long id,
    // @Valid @RequestBody UserRequest request) {
    // UserResponse response = userService.updateUser(id, request);
    // return ResponseEntity.ok(ApiResponse.success("User updated successfully",
    // response));
    // }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete user")
    // public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
    // userService.deleteUser(id);
    // return ResponseEntity.ok(ApiResponse.success("User deleted successfully",
    // null));
    // }
}
