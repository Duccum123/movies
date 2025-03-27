package com.example.phimmoi.service;

import com.example.phimmoi.dto.request.GenreRequest;
import com.example.phimmoi.dto.response.ApiResponse;
import com.example.phimmoi.dto.response.GenreResponse;
import com.example.phimmoi.entity.Genre;
import com.example.phimmoi.exception.AppException;
import com.example.phimmoi.exception.ErrorCode;
import com.example.phimmoi.mapper.GenreMapper;
import com.example.phimmoi.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GenreService {
    @Autowired
    private GenreMapper genreMapper;
    @Autowired
    private GenreRepository genreRepository;

    public GenreResponse createGenre(GenreRequest request){
        if(genreRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.GENRE_NAME_ALREADY_EXISTS);
        Genre genre = genreMapper.toGenre(request);

        System.out.println(request.getName());
        return genreMapper.toGenreResponse(genreRepository.save(genre));
    }
    public List<GenreResponse> getAll(){
        return genreRepository.findAll().stream()
                .map(genreMapper::toGenreResponse).toList();
    }
    public GenreResponse getById(String id){
        return genreMapper.toGenreResponse(genreRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GENRE_NOT_FOUND)));
    }
    public GenreResponse updateGenre(String id, GenreRequest request){
        Genre genreUpdate = genreRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GENRE_NOT_FOUND));
        genreUpdate.setName(request.getName());
        return genreMapper.toGenreResponse(genreRepository.save(genreUpdate));
    }
    public void deleteGenre(String id){
        genreRepository.deleteById(id);
    }
}
