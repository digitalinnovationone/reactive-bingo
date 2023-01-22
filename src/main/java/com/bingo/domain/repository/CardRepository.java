package com.bingo.domain.repository;

import com.bingo.domain.document.CardDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CardRepository extends ReactiveMongoRepository<CardDocument, String> {
    Flux<CardDocument> findByPlayerId(final String playerId);
    Flux<CardDocument> findByRoundId(final String roundId);
    Mono<CardDocument> findByPlayerIdAndRoundId(final String playerId, final String roundId);
}
