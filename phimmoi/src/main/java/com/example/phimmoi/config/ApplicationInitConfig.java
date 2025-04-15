package com.example.phimmoi.config;

import com.example.phimmoi.entity.Permission;
import com.example.phimmoi.entity.Role;
import com.example.phimmoi.entity.User;
import com.example.phimmoi.repository.PermissionRepository;
import com.example.phimmoi.repository.RoleRepository;
import com.example.phimmoi.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                        RoleRepository roleRepository,
                                        PermissionRepository permissionRepository,
                                        PasswordEncoder passwordEncoder) {
        return args -> {
            // Tạo permission nếu chưa có
            Permission read_admin = permissionRepository.findByName("READ_ADMIN")
                    .orElseGet(() -> permissionRepository.save(Permission.builder().name("READ_ADMIN").build()));
            Permission write_admin = permissionRepository.findByName("WRITE_ADMIN")
                    .orElseGet(() -> permissionRepository.save(Permission.builder().name("WRITE_ADMIN").build()));
            Permission delete_admin = permissionRepository.findByName("DELETE_ADMIN")
                    .orElseGet(() -> permissionRepository.save(Permission.builder().name("DELETE_ADMIN").build()));

            Permission read_user = permissionRepository.findByName("READ_USER")
                    .orElseGet(() -> permissionRepository.save(Permission.builder().name("READ_USER").build()));
            Permission write_user = permissionRepository.findByName("WRITE_USER")
                    .orElseGet(() -> permissionRepository.save(Permission.builder().name("WRITE_USER").build()));
            Permission delete_user = permissionRepository.findByName("DELETE_USER")
                    .orElseGet(() -> permissionRepository.save(Permission.builder().name("DELETE_USER").build()));


            if(roleRepository.findByName("user").isEmpty()) {
                Role role = Role.builder().name("user").permissions(Set.of(read_user,write_user,delete_user)).build();
                roleRepository.save(role);
            }

            // Tạo role admin nếu chưa có và gán permission
            Role adminRole = roleRepository.findByName("admin").orElse(null);
            if (adminRole == null) {
                adminRole = Role.builder()
                        .name("admin")
                        .permissions(Set.of(read_admin, write_admin, delete_admin))
                        .build();
                roleRepository.save(adminRole);
            }

            // Tạo user admin nếu chưa có
            if (userRepository.findByUsername("admin").isEmpty()) {
                String hashedPassword = passwordEncoder.encode("admin");
                User user = User.builder()
                        .username("admin")
                        .password(hashedPassword)
                        .roles(Set.of(adminRole))
                        .build();
                userRepository.save(user);
                log.warn("Tạo admin thành công: username = admin, password = admin");
            }
        };
    }

}
