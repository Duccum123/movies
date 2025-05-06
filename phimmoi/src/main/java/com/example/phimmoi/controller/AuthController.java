package com.example.phimmoi.controller;

import com.example.phimmoi.dto.request.LoginRequest;
import com.example.phimmoi.dto.request.TokenRequest;
import com.example.phimmoi.dto.response.ApiResponse;
import com.example.phimmoi.dto.response.JwtResponse;
import com.example.phimmoi.utils.JwtTokenProvider;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.text.ParseException;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private RestClient.Builder builder;

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @PostMapping("/login")
    public ApiResponse<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        String token = jwtTokenProvider.generateToken(auth.getName());
        ApiResponse<JwtResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(new JwtResponse(token));
        apiResponse.setMessage("token generated");
        return apiResponse;
    }
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody TokenRequest request) throws JOSEException, ParseException {
        jwtTokenProvider.logout(request.getToken());
        return ApiResponse.<Void>builder()
                .build();
    }
    @PostMapping("/refresh")
    public ApiResponse<JwtResponse> refresh(@RequestBody TokenRequest request) throws JOSEException, ParseException {
        String token = jwtTokenProvider.refreshToken(request.getToken());
        ApiResponse<JwtResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(new JwtResponse(token));
        apiResponse.setMessage("token generated");
        return apiResponse;
    }
}

