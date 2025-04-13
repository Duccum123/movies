package com.example.phimmoi.config;

import com.example.phimmoi.entity.User;
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

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()) {
                String hashedPassword  = passwordEncoder.encode("admin");
                User user = User.builder()
                        .username("admin")
                        .password(hashedPassword)
                        .role("admin")
                        .build();
                userRepository.save(user);
                log.warn("tao admin thanh cong, username: admin, password: admin");
            }
        };
    }
}
