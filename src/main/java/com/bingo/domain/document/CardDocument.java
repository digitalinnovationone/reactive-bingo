package com.bingo.domain.document;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashSet;

@Document(collection = "cards")
@Builder
public record CardDocument(
        @Id
        String id,
        String playerId,
        String roundId,
        LinkedHashSet<Integer> numbers) {
}
