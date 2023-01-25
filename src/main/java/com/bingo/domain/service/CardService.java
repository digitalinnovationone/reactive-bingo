package com.bingo.domain.service;

import com.bingo.api.response.CardResponse;
import com.bingo.api.response.PageResponse;
import com.bingo.domain.repository.CardRepository;
import com.bingo.domain.service.query.CardQueryService;
import com.bingo.util.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CardService {
        private final CardRepository cardRepository;
        private final CardQueryService cardQueryService;
        private final CardMapper cardMapper;

    public Mono<PageResponse> findAllCards(final long page, final long limit) {
        return cardRepository.count()
                .zipWith(cardRepository.findAll()
                        .skip((page - 1) * limit)
                        .take(limit)
                        .map(cardMapper::toCardResponse)
                        .collectList())
                .map(zip -> PageResponse.builder()
                        .page(page)
                        .limit(limit)
                        .total(zip.getT1())
                        .itens(zip.getT2())
                        .build());
    }

    public Mono<CardResponse> findCardByCardId(final String cardId) {
        return cardQueryService.findCardByCardId(cardId)
                .map(cardMapper::toCardResponse);
    }

    public Flux<CardResponse> findCardByPlayerId(final String userId) {
        return cardQueryService.findCardByPlayerId(userId)
                .map(cardMapper::toCardResponse);
    }

    public Flux<CardResponse> findCardByRoundId(final String roundId) {
        return cardQueryService.findCardByRoundId(roundId)
                .map(cardMapper::toCardResponse);
    }
}
