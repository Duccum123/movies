package com.example.phimmoi.repository;

import com.example.phimmoi.entity.Movie_Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Movie_GenreRepository  extends JpaRepository<Movie_Genre, String> {
}
