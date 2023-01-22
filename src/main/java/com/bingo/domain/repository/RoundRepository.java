package com.bingo.domain.repository;

import com.bingo.domain.document.RoundDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RoundRepository extends ReactiveMongoRepository<RoundDocument, String> {
    Mono<RoundDocument> findTop1ByOrderByCreatedAtDesc();
}
