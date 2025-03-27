package com.example.phimmoi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Movie_Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_movie_genre_movie"))
    Movie movie;

    @ManyToOne
    @JoinColumn(name = "genre_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_movie_genre_genre"))
    Genre genre;
}
