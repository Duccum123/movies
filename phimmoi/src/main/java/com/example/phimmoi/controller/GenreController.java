package com.example.phimmoi.controller;


import com.example.phimmoi.dto.request.GenreRequest;
import com.example.phimmoi.dto.response.ApiResponse;
import com.example.phimmoi.dto.response.GenreResponse;
import com.example.phimmoi.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    @Autowired
    public GenreService genreService;

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<GenreResponse> createGenre(@RequestBody GenreRequest request) {
        ApiResponse<GenreResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(genreService.createGenre(request));
        return apiResponse;
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<List<GenreResponse>> getAllGenre() {
        ApiResponse<List<GenreResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(genreService.getAll());
        return apiResponse;
    }
    @GetMapping("/getGenreById/{id}")
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<GenreResponse> getGenreById(@PathVariable("id") String id) {
        ApiResponse<GenreResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(genreService.getById(id));
        return apiResponse;
    }
    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<GenreResponse> updateGenre(@PathVariable("id") String id, @RequestBody GenreRequest request) {
        ApiResponse<GenreResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(genreService.updateGenre(id, request));
        return apiResponse;
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('admin')")
    public void deleteGenre(@PathVariable("id") String id) {
        genreService.deleteGenre(id);
    }
}
