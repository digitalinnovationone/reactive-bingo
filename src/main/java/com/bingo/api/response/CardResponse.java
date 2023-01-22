package com.bingo.api.response;

import lombok.Builder;

import java.util.LinkedHashSet;

@Builder
public record CardResponse(
        String id,
        String playerId,
        String roundId,
        LinkedHashSet<Integer> numbers) {
}
