package com.example.phimmoi.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class LoginRequest {
    String username;
    @Size(min = 6, max = 20)
    String password;
}
