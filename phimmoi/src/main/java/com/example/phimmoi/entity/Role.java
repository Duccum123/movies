package com.example.phimmoi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    @GeneratedValue
    long id;
    String name;

    @ManyToMany(fetch = FetchType.EAGER)
            @JoinTable(
                    name = "role_permissions",
                    joinColumns = @JoinColumn(name = "role_id"),
                    inverseJoinColumns = @JoinColumn(name = "permission_id")
            )
    Set<Permission> permissions;
}
