package com.bingo.api.response;

import com.bingo.domain.document.RoundDocument.RoundStatus;
import lombok.Builder;

import java.util.LinkedHashSet;

@Builder
public record RoundResponse(
        String id,
        RoundStatus status,
        LinkedHashSet<Integer> numbers) {
}
