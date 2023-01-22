package com.bingo.domain.exception;

import com.bingo.api.response.ExceptionResponse;

public abstract class BingoException extends RuntimeException {
    public final ExceptionResponse exceptionResponse;

    public BingoException(final ExceptionResponse exceptionResponse) {
        this.exceptionResponse = exceptionResponse;
    }
}
