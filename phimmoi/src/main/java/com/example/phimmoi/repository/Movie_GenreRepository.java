package com.example.phimmoi.repository;

import com.example.phimmoi.entity.Movie_Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Movie_GenreRepository  extends JpaRepository<Movie_Genre, String> {
    List<Movie_Genre> findByGenreId(String genreId);
}
