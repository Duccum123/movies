package com.example.phimmoi.controller;

import com.example.phimmoi.dto.request.UserRequest;
import com.example.phimmoi.dto.response.ApiResponse;
import com.example.phimmoi.dto.response.UserResponse;
import com.example.phimmoi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('admin')")
    ApiResponse<List<UserResponse>> getAllUser() {
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllUser());
        return apiResponse;
    }

    @GetMapping("/getUserById/{id}")
    @PreAuthorize("hasRole('admin') or #id == authentication.principal.id")
    ApiResponse<UserResponse> getUserById(@PathVariable("id") String id) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUserById(id));
        return apiResponse;
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('admin') or #id == authentication.principal.id")
    ApiResponse<UserResponse> updateUser(@PathVariable("id") String id, @RequestBody UserRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateUser(id, request));
        return apiResponse;
    }
    @DeleteMapping("/deleteSoftById/{id}")
    @PreAuthorize("hasRole('admin')")
    ApiResponse<UserResponse> deleteUser(@PathVariable("id") String id) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.deleteSoftUser(id));
        return apiResponse;
    }
}
