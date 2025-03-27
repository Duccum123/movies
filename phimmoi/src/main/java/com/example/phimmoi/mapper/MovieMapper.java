package com.example.phimmoi.mapper;

import com.example.phimmoi.dto.request.MovieRequest;
import com.example.phimmoi.dto.response.MovieResponse;
import com.example.phimmoi.entity.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie toMovie(MovieRequest movieRequest);

    MovieResponse toMovieResponse(Movie movie);
}
