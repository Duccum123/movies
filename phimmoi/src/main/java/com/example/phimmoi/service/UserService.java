package com.example.phimmoi.service;

import com.example.phimmoi.dto.request.UserRequest;
import com.example.phimmoi.dto.response.UserResponse;
import com.example.phimmoi.entity.Role;
import com.example.phimmoi.entity.User;
import com.example.phimmoi.exception.AppException;
import com.example.phimmoi.exception.ErrorCode;
import com.example.phimmoi.mapper.UserMapper;
import com.example.phimmoi.repository.RoleRepository;
import com.example.phimmoi.repository.UserRepository;
import com.example.phimmoi.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;


    public UserResponse createUser(UserRequest request) {

        if(userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        User user = userMapper.toUser(request);
        user.setEnabled(true);
        // mã hóa mật khẩu trước khi lưu, hàm passwordEncoder.matches(rawPassword, hashedPassword) để so sánh lại mật khẩu đăng nhập và mật khẩu đã mã hóa lưu trong db
        String hashedPassword  = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        Set<Role> roles = request.getRoles().stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + name)))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public List<UserResponse> getAllUser() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }
    public UserResponse getUserById(String id) {

        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }
    public UserResponse updateUser(String id, UserRequest request){
        User userUpdate = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userUpdate.setUsername(request.getUsername());
        userUpdate.setPassword(request.getPassword());
        userUpdate.setEmail(request.getEmail());
        Set<Role> roles = request.getRoles().stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + name)))
                .collect(Collectors.toSet());
        userUpdate.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(userUpdate));
    }
    public UserResponse deleteSoftUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setEnabled(false);
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public String deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
        return "User deleted";
    }
}
