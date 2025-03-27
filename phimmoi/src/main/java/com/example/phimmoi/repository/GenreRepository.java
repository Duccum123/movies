package com.example.phimmoi.repository;

import com.example.phimmoi.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, String> {
    boolean existsByName(String name);
}
