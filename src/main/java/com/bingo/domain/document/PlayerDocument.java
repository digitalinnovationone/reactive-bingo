package com.bingo.domain.document;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "players")
@Builder
public record PlayerDocument(
        @Id
        String id,
        String name,
        String email) {
}
