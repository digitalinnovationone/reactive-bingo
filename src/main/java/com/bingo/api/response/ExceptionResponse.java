package com.bingo.api.response;

import lombok.Builder;

@Builder
public record ExceptionResponse(
        int status,
        String error,
        String message) {
}
