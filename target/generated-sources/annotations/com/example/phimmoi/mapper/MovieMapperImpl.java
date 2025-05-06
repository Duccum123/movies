package com.example.phimmoi.mapper;

import com.example.phimmoi.dto.request.MovieRequest;
import com.example.phimmoi.dto.response.MovieResponse;
import com.example.phimmoi.entity.Movie;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T21:01:01+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Amazon.com Inc.)"
)
@Component
public class MovieMapperImpl implements MovieMapper {

    @Override
    public Movie toMovie(MovieRequest movieRequest) {
        if ( movieRequest == null ) {
            return null;
        }

        Movie movie = new Movie();

        movie.setTitle( movieRequest.getTitle() );
        movie.setRelease_year( movieRequest.getRelease_year() );
        movie.setDescription( movieRequest.getDescription() );
        movie.setDuration( movieRequest.getDuration() );
        movie.setLanguage( movieRequest.getLanguage() );
        movie.setPoster( movieRequest.getPoster() );
        movie.setTrailer( movieRequest.getTrailer() );
        movie.setDirector( movieRequest.getDirector() );

        return movie;
    }

    @Override
    public MovieResponse toMovieResponse(Movie movie) {
        if ( movie == null ) {
            return null;
        }

        MovieResponse movieResponse = new MovieResponse();

        movieResponse.setId( movie.getId() );
        movieResponse.setTitle( movie.getTitle() );
        movieResponse.setRelease_year( movie.getRelease_year() );
        movieResponse.setDescription( movie.getDescription() );
        movieResponse.setDuration( movie.getDuration() );
        movieResponse.setLanguage( movie.getLanguage() );
        movieResponse.setPoster( movie.getPoster() );
        movieResponse.setTrailer( movie.getTrailer() );
        movieResponse.setDirector( movie.getDirector() );
        movieResponse.setCreated_at( movie.getCreated_at() );
        movieResponse.setEnabled( movie.isEnabled() );

        return movieResponse;
    }
}
