package com.bingo.domain.service;

import com.bingo.api.request.CardRequest;
import com.bingo.api.response.CardResponse;
import com.bingo.api.response.PageResponse;
import com.bingo.api.response.RoundResponse;
import com.bingo.domain.document.CardDocument;
import com.bingo.domain.exception.PlayerAlreadyEnrollException;
import com.bingo.domain.exception.RoundNotFinishedException;
import com.bingo.domain.exception.RoundNotOpenException;
import com.bingo.domain.exception.RoundSizeException;
import com.bingo.domain.repository.CardRepository;
import com.bingo.domain.repository.RoundRepository;
import com.bingo.domain.service.query.CardQueryService;
import com.bingo.domain.service.query.RoundQueryService;
import com.bingo.util.generator.CardGenerator;
import com.bingo.util.generator.RoundGenerator;
import com.bingo.util.mapper.CardMapper;
import com.bingo.util.mapper.RoundMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.LinkedHashSet;
import java.util.Optional;

import static com.bingo.domain.document.RoundDocument.RoundStatus.FINISHED;
import static com.bingo.domain.document.RoundDocument.RoundStatus.OPEN;
import static com.bingo.util.generator.CardGenerator.ROUND_SIZE;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoundService {
    private final CardRepository cardRepository;
    private final RoundRepository roundRepository;
    private final CardQueryService cardQueryService;
    private final RoundQueryService roundQueryService;
    private final CardMapper cardMapper;
    private final RoundMapper roundMapper;
    private final CardGenerator cardGenerator;
    private final RoundGenerator roundGenerator;

    public Mono<PageResponse> findAllRounds(final long page, final long limit) {
        return roundRepository.count()
                .zipWith(roundRepository.findAll()
                        .skip((page - 1) * limit)
                        .take(limit)
                        .map(roundMapper::toRoundResponse)
                        .collectList())
                .map(zip -> PageResponse.builder()
                        .page(page)
                        .limit(limit)
                        .total(zip.getT1())
                        .itens(zip.getT2())
                        .build());
    }

    public Mono<RoundResponse> findRound(final String roundId) {
        return roundQueryService.findRoundByRoundId(roundId)
                .map(roundMapper::toRoundResponse);
    }

    public Mono<RoundResponse> createRound() {
        return roundRepository.findTop1ByOrderByCreatedAtDesc()

                .flatMap(round -> Mono.just(round)
                        .filter(__ -> round.status() == FINISHED || round.numbers().size() >= ROUND_SIZE)
                        .map(__ -> roundGenerator.newRound())
                        .switchIfEmpty(Mono.defer(() -> Mono.error(RoundNotFinishedException::new)))
                )

                .switchIfEmpty(Mono.defer(() -> Mono.just(roundGenerator.newRound())))

                .flatMap(roundRepository::save)
                .map(roundMapper::toRoundResponse);
    }

    public Mono<LinkedHashSet<Integer>> findWinningNumbers(final String roundId) {
        return roundQueryService.findWinningsNumbersByRoundId(roundId);
    }

    public Mono<Integer> findLastNumber(final String roundId) {
        return roundQueryService.findLastNumberByRoundId(roundId);
    }

    public Mono<Integer> findNewNumber(final String roundId) {
        return roundQueryService.findRoundByRoundId(roundId)

                .filter(round -> round.status() != FINISHED)
                .switchIfEmpty(Mono.defer(() -> Mono.error(RoundNotOpenException::new)))

                .filter(round -> round.numbers().size() < ROUND_SIZE)
                .switchIfEmpty(Mono.defer(() -> Mono.error(RoundSizeException::new)))

                .map(roundGenerator::addNewNumberToRound)
                .flatMap(roundRepository::save)

                .flatMap(round -> roundQueryService.findWinningsNumbersByRoundId(roundId)
                        .flatMap(__ -> Mono.just(roundGenerator.finishedRound(round))
                                .flatMap(roundRepository::save))
                        .switchIfEmpty(Mono.defer(() -> Mono.just(round))))

                .map(round -> round.numbers().stream()
                        .reduce((__, last) -> last))

                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public Mono<CardResponse> enrollPlayer(final CardRequest cardRequest) {
        return roundRepository.findById(cardRequest.roundId())
                .filter(round -> round.status() == OPEN)
                .switchIfEmpty(Mono.defer(() -> Mono.error(RoundNotOpenException::new)))

                .flatMap(round -> cardQueryService.findCardByPlayerIdAndRoundId(cardRequest.playerId(), cardRequest.roundId())
                        .hasElement()
                        .filter(element -> !element))
                .switchIfEmpty(Mono.defer(() -> Mono.error(PlayerAlreadyEnrollException::new)))

                .flatMap(round -> cardQueryService.findCardByRoundId(cardRequest.roundId())
                        .collectList())
                .map(cardGenerator::newCard)

                .map(card -> CardDocument.builder()
                        .playerId(cardRequest.playerId())
                        .roundId(cardRequest.roundId())
                        .numbers(card.numbers())
                        .build())

                .flatMap(cardRepository::save)
                .map(cardMapper::toCardResponse);
    }
}
