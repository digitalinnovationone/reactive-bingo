package com.bingo.api.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponse(
        List<?> itens,
        long page,
        long limit,
        long total) {
}
