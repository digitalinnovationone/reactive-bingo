package com.bingo.domain.repository;

import com.bingo.domain.document.PlayerDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerRepository extends ReactiveMongoRepository<PlayerDocument, String> {
    Mono<PlayerDocument> findByEmail(final String email);
}
