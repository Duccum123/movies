package com.example.phimmoi.mapper;

import com.example.phimmoi.dto.request.GenreRequest;
import com.example.phimmoi.dto.response.GenreResponse;
import com.example.phimmoi.entity.Genre;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Amazon.com Inc.)"
)
@Component
public class GenreMapperImpl implements GenreMapper {

    @Override
    public Genre toGenre(GenreRequest genreRequest) {
        if ( genreRequest == null ) {
            return null;
        }

        Genre genre = new Genre();

        genre.setName( genreRequest.getName() );

        return genre;
    }

    @Override
    public Genre toGenre(GenreResponse genreResponse) {
        if ( genreResponse == null ) {
            return null;
        }

        Genre genre = new Genre();

        genre.setId( genreResponse.getId() );
        genre.setName( genreResponse.getName() );
        genre.setEnabled( genreResponse.isEnabled() );

        return genre;
    }

    @Override
    public GenreResponse toGenreResponse(Genre genre) {
        if ( genre == null ) {
            return null;
        }

        GenreResponse genreResponse = new GenreResponse();

        genreResponse.setName( genre.getName() );
        genreResponse.setId( genre.getId() );
        genreResponse.setEnabled( genre.isEnabled() );

        return genreResponse;
    }
}
