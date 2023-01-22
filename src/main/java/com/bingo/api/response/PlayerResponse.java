package com.bingo.api.response;

import lombok.Builder;

@Builder
public record PlayerResponse(
        String id,
        String name,
        String email) {
}
