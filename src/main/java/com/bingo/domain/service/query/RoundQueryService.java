package com.bingo.domain.service.query;

import com.bingo.domain.document.RoundDocument;
import com.bingo.domain.exception.RoundNotFoundException;
import com.bingo.domain.repository.CardRepository;
import com.bingo.domain.repository.RoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.LinkedHashSet;
import java.util.Optional;

import static com.bingo.util.generator.CardGenerator.HITS_TO_WIN;

@Service
@RequiredArgsConstructor
public class RoundQueryService {
    private final CardRepository cardRepository;
    private final RoundRepository roundRepository;

    public Mono<RoundDocument> findRoundByRoundId(final String roundId) {
        return roundRepository.findById(roundId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(RoundNotFoundException::new)));
    }

    public Mono<Integer> findLastNumberByRoundId(final String roundId) {
        return roundRepository.findById(roundId)
                .map(round -> round.numbers().stream()
                        .reduce((__, last) -> last))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public Mono<LinkedHashSet<Integer>> findWinningsNumbersByRoundId(final String roundId) {
        return roundRepository.findById(roundId)
                .flatMapMany(round -> cardRepository.findByRoundId(roundId)
                        .map(card -> Tuples.of(round.numbers(), card.numbers())))
                .map(tuple -> {
                    LinkedHashSet<Integer> hits = new LinkedHashSet<>(tuple.getT2());
                    hits.retainAll(tuple.getT1());
                    return hits;})
                .filter(hits -> hits.size() >= HITS_TO_WIN)
                .take(1)
                .next();
    }
}
