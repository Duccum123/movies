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
    @PreAuthorize("hasAnyAuthority('WRITE_ADMIN')")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('READ_ADMIN')")
    ApiResponse<List<UserResponse>> getAllUser() {
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllUser());
        return apiResponse;
    }

    @GetMapping("/getUserById/{id}")
    @PreAuthorize("hasAuthority('READ_ADMIN') or (#id == authentication.principal.id and hasAuthority('READ_USER'))")
    ApiResponse<UserResponse> getUserById(@PathVariable("id") String id) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUserById(id));
        return apiResponse;
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('WRITE') or (#id == authentication.principal.id and hasAuthority('READ_USER'))")
    ApiResponse<UserResponse> updateUser(@PathVariable("id") String id, @RequestBody UserRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateUser(id, request));
        return apiResponse;
    }
    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasAuthority('DELETE_ADMIN')")
    ApiResponse<String> deleteById(@PathVariable("id") String id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.deleteUser(id));
        return apiResponse;
    }
    @GetMapping("/deleteSoftById/{id}")
    @PreAuthorize("hasAuthority('DELETE_ADMIN')")
    ApiResponse<UserResponse> deleteSoftById(@PathVariable("id") String id) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.deleteSoftUser(id));
        return apiResponse;
    }
}
