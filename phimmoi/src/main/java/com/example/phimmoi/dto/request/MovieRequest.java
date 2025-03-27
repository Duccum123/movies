package com.example.phimmoi.dto.request;

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
public class MovieRequest {
    String title;
    int release_year;
    String description;
    int duration;
    String language;
    String poster;
    String trailer;
    String director;
    List<String> id_genres;
}
