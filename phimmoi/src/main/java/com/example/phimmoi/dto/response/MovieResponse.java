package com.example.phimmoi.dto.response;

import com.example.phimmoi.entity.Genre;
import com.example.phimmoi.entity.Movie_Genre;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieResponse {
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
    List<String> genres;
    boolean isEnabled;
}
