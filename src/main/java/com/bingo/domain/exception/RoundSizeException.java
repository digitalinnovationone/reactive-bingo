package com.bingo.domain.exception;

import com.bingo.api.response.ExceptionResponse;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class RoundSizeException extends BingoException {
    public RoundSizeException() {
        super(ExceptionResponse.builder()
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.name())
                .message("ROUND_SIZE_REACHED")
                .build());
    }
}
