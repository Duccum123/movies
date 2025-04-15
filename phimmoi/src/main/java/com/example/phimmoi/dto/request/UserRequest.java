package com.example.phimmoi.dto.request;


import com.example.phimmoi.entity.Role;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

    String username;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password;
    String email;
    Set<String> roles;
}
