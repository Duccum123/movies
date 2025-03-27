package com.example.phimmoi.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found"),
    EMAIL_ALREADY_EXISTS(1002, "Email already exists"),
    USERNAME_ALREADY_EXISTS(1003, "Username already exists"),
    GENRE_NAME_ALREADY_EXISTS(1004, "Genre name already exists"),
    GENRE_NOT_FOUND(1005, "Genre not found"),
    MOVIE_NOT_FOUND(1006, "Movie not found"),
    ;
    final int code;
    final String message;
}
