package com.bingo.domain.document;

import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;

@Document(collection = "rounds")
@Builder
public record RoundDocument(
        @Id
        String id,
        RoundStatus status,
        LinkedHashSet<Integer> numbers,
        @CreatedDate
        LocalDateTime createdAt,
        @LastModifiedDate
        LocalDateTime updatedAt) {

        public enum RoundStatus {
                OPEN, CLOSED, FINISHED;
        }
}
