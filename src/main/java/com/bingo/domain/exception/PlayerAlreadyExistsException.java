package com.bingo.domain.exception;

import com.bingo.api.response.ExceptionResponse;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class PlayerAlreadyExistsException extends BingoException {
    public PlayerAlreadyExistsException() {
        super(ExceptionResponse.builder()
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.name())
                .message("PLAYER_ALREADY_EXISTS")
                .build());
    }
}
