package com.example.phimmoi.service;

import com.example.phimmoi.entity.User;
import com.example.phimmoi.exception.AppException;
import com.example.phimmoi.exception.ErrorCode;
import com.example.phimmoi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority("role_" + user.getRole()))
        );
    }
}
