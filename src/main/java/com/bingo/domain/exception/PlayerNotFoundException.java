package com.bingo.domain.exception;

import com.bingo.api.response.ExceptionResponse;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class PlayerNotFoundException extends BingoException {
    public PlayerNotFoundException() {
        super(ExceptionResponse.builder()
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.name())
                .message("PLAYER_NOT_FOUND")
                .build());
    }
}
