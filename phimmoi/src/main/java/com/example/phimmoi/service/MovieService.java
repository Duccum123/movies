package com.example.phimmoi.service;

import com.example.phimmoi.dto.request.MovieRequest;
import com.example.phimmoi.dto.request.UpdatePosterMovieRequest;
import com.example.phimmoi.dto.response.GenreResponse;
import com.example.phimmoi.dto.response.MovieResponse;
import com.example.phimmoi.entity.Genre;
import com.example.phimmoi.entity.Movie;
import com.example.phimmoi.entity.Movie_Genre;
import com.example.phimmoi.exception.AppException;
import com.example.phimmoi.exception.ErrorCode;
import com.example.phimmoi.mapper.GenreMapper;
import com.example.phimmoi.mapper.MovieMapper;
import com.example.phimmoi.repository.MovieRepository;
import com.example.phimmoi.repository.Movie_GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    MovieMapper movieMapper;
    @Autowired
    Movie_GenreRepository movie_GenreRepository;
    @Autowired
    GenreService genreService;
    @Autowired
    GenreMapper genreMapper;
    @Autowired
    Movie_GenreRepository movieGenreRepository;

    public MovieResponse createMovie(MovieRequest request) {
        Movie movie = movieMapper.toMovie(request);
        movie.setCreated_at(LocalDateTime.now());
        String movieId = UUID.randomUUID().toString();
        movie.setId(movieId);
        movie.setEnabled(true);
        movieRepository.save(movie);

        List<Movie_Genre> movieGenres = new ArrayList<>();
        request.getId_genres().forEach(genreId -> {
            Movie_Genre movieGenre = new Movie_Genre();

            movieGenre.setMovie(movie);

            GenreResponse genreResponse = genreService.getById(genreId);
            Genre genre = genreMapper.toGenre(genreResponse);
            movieGenre.setGenre(genre);

            movieGenres.add(movieGenre);
        });

        movie_GenreRepository.saveAll(movieGenres);

        movie.setMovie_genres(movieGenres);
        MovieResponse movieResponse = movieMapper.toMovieResponse(movie);
        movieResponse.setGenres(movieGenres.stream().map(movieGenre -> movieGenre.getGenre().getName()).collect(Collectors.toList()));
        return movieResponse;
    }
    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAllWithGenres().stream()
                .map(movie -> {
                    MovieResponse movieResponse = movieMapper.toMovieResponse(movie);

                    List<GenreResponse> genreResponses = movie.getMovie_genres().stream()
                            .map(movie_genre -> genreMapper.toGenreResponse(movie_genre.getGenre()))
                            .toList();

                    movieResponse.setGenres(genreResponses.stream().map(GenreResponse::getName).collect(Collectors.toList()));
                    return movieResponse;
                })
                .collect(Collectors.toList());
    }
    public List<MovieResponse> getMoviesByIdGenre(String id) {
        List<Movie_Genre> movieGenres = movie_GenreRepository.findByGenreId(id);
        List<MovieResponse> movieResponses = new ArrayList<>();
        for (Movie_Genre movieGenre : movieGenres) {
            Movie movie = movieRepository.findById(movieGenre.getMovie().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
            MovieResponse movieResponse = movieMapper.toMovieResponse(movie);
            movieResponses.add(movieResponse);
        }
        return movieResponses;
    }
    public MovieResponse getById(String id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        MovieResponse movieResponse = movieMapper.toMovieResponse(movie);
        movieResponse.setGenres(movie.getMovie_genres().stream().map(movieGenre -> movieGenre.getGenre().getName()).collect(Collectors.toList()));
        return movieResponse;
    }
    public MovieResponse deleteSoftById(String id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        movie.setEnabled(false);
         return movieMapper.toMovieResponse(movieRepository.save(movie));
    }
    public String deleteById(String id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        movieRepository.delete(movie);
        return "movie deleted";
    }
    public MovieResponse updatePosterMovie(UpdatePosterMovieRequest request) {
        Movie movie = movieRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        movie.setPoster(request.getPoster());
        return movieMapper.toMovieResponse(movieRepository.save(movie));
    }
}
