package com.example.phimmoi.mapper;

import com.example.phimmoi.dto.request.GenreRequest;
import com.example.phimmoi.dto.response.GenreResponse;
import com.example.phimmoi.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    @Mapping(source = "name", target = "name")
    Genre toGenre(GenreRequest genreRequest);

    Genre toGenre(GenreResponse genreResponse);
    @Mapping(source = "name", target = "name")
    @Mapping(source = "id", target = "id")
    GenreResponse toGenreResponse(Genre genre);
}
