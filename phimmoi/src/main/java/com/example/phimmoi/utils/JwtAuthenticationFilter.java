package com.example.phimmoi.utils;

import com.example.phimmoi.dto.response.ApiResponse;
import com.example.phimmoi.exception.AppException;
import com.example.phimmoi.exception.ErrorCode;
import com.example.phimmoi.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                if (!jwtTokenProvider.validateToken(token, false)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                    response.setContentType("application/json");

                    ApiResponse<String> apiResponse = new ApiResponse<>();
                    apiResponse.setCode(ErrorCode.UNAUTHENTICATED.getCode());
                    apiResponse.setMessage(ErrorCode.UNAUTHENTICATED.getMessage());

                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(apiResponse);
                    response.getWriter().write(json);
                    return;
                } else {
                    String username = jwtTokenProvider.getUsernameFromToken(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            filterChain.doFilter(request, response);
        } catch (AppException e) {
            response.setStatus(HttpServletResponse.SC_OK);

            response.setContentType("application/json");

            ApiResponse<String> apiResponse = new ApiResponse<>();
            apiResponse.setCode(e.getErrorCode().getCode());
            apiResponse.setMessage(e.getErrorCode().getMessage());

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(apiResponse);

            response.getWriter().write(json);
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}

