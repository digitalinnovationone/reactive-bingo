package com.bingo.domain.service.query;

import com.bingo.domain.document.CardDocument;
import com.bingo.domain.exception.CardNotFoundException;
import com.bingo.domain.exception.PlayerNotFoundException;
import com.bingo.domain.exception.RoundNotFoundException;
import com.bingo.domain.repository.CardRepository;
import com.bingo.domain.repository.PlayerRepository;
import com.bingo.domain.repository.RoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CardQueryService {
    private final CardRepository cardRepository;
    private final PlayerRepository playerRepository;
    private final RoundRepository roundRepository;

    public Mono<CardDocument> findCardByCardId(final String cardId) {
        return cardRepository.findById(cardId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(CardNotFoundException::new)));
    }

    public Flux<CardDocument> findCardByPlayerId(final String playerId) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(PlayerNotFoundException::new)))
                .flatMapMany(player -> cardRepository.findByPlayerId(playerId));
    }

    public Flux<CardDocument> findCardByRoundId(final String roundId) {
        return roundRepository.findById(roundId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(RoundNotFoundException::new)))
                .flatMapMany(round -> cardRepository.findByRoundId(roundId));
    }

    public Mono<CardDocument> findCardByPlayerIdAndRoundId(final String playerId, final String roundId) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(PlayerNotFoundException::new)))
                .flatMap(player -> roundRepository.findById(roundId))
                .switchIfEmpty(Mono.defer(() -> Mono.error(RoundNotFoundException::new)))
                .flatMap(round -> cardRepository.findByPlayerIdAndRoundId(playerId, roundId));
    }
}
