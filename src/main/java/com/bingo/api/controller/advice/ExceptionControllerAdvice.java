package com.bingo.api.controller.advice;

import com.bingo.api.response.ExceptionResponse;
import com.bingo.domain.exception.BingoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(BingoException.class)
    public Mono<ResponseEntity<ExceptionResponse>> bingoException(final BingoException e) {
        return Mono.just(ResponseEntity
                .status(e.exceptionResponse.status())
                .contentType(APPLICATION_JSON)
                .body(e.exceptionResponse));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ExceptionResponse>> constraintViolationException(final ConstraintViolationException e) {
        return Mono.just(ResponseEntity
                .status(BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(ExceptionResponse.builder()
                        .status(BAD_REQUEST.value())
                        .error("INVALID_DATA")
                        .message(e.getMessage())
                        .build()));
    }
}
