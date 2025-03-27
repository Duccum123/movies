package com.example.phimmoi.repository;

import com.example.phimmoi.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.movie_genres mg LEFT JOIN FETCH mg.genre")
    List<Movie> findAllWithGenres();
}
