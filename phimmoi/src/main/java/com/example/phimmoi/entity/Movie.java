package com.example.phimmoi.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Movie {
    @Id
    @NotNull
    String id;
    String title;
    int release_year;
    String description;
    int duration;
    String language;
    String poster;
    String trailer;
    String director;
    LocalDateTime created_at;
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Movie_Genre> movie_genres;
    boolean isEnabled;
}
