package com.example.phimmoi.controller;


import com.example.phimmoi.dto.request.MovieRequest;
import com.example.phimmoi.dto.response.ApiResponse;
import com.example.phimmoi.dto.response.MovieResponse;
import com.example.phimmoi.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    MovieService movieService;

    @PostMapping
    public ApiResponse<MovieResponse> createMovie(@RequestBody MovieRequest request) {
        ApiResponse<MovieResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(movieService.createMovie(request));
        return apiResponse;
    }
    @GetMapping("/getAll")
    public ApiResponse<List<MovieResponse>> getAllMovies() {
        ApiResponse<List<MovieResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(movieService.getAllMovies());
        return apiResponse;
    }
    @GetMapping("/getById/{id}")
    public ApiResponse<MovieResponse> getMovieById(@PathVariable("id") String id) {
        ApiResponse<MovieResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(movieService.getById(id));
        return apiResponse;
    }
    @DeleteMapping("/deleteSoftById/{id}")
    public ApiResponse<MovieResponse> deleteSoftById(@PathVariable("id") String id) {
        ApiResponse<MovieResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(movieService.deleteSoftById(id));
        return apiResponse;
    }

}
