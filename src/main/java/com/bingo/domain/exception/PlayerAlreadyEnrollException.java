package com.bingo.domain.exception;

import com.bingo.api.response.ExceptionResponse;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class PlayerAlreadyEnrollException extends BingoException {
    public PlayerAlreadyEnrollException() {
        super(ExceptionResponse.builder()
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.name())
                .message("PLAYER_ALREADY_ENROLL")
                .build());
    }
}
