package com.bingo.api.controller;

import com.bingo.api.response.CardResponse;
import com.bingo.api.response.PageResponse;
import com.bingo.domain.service.CardService;
import com.bingo.util.validator.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
@Validated
public class CardController {
    private final CardService cardService;

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/all")
    @ResponseStatus(OK)
    public Mono<PageResponse> findAllCards(
            @RequestParam(value = "page", required = false, defaultValue = "1") final long page,
            @RequestParam(value = "limit", required = false, defaultValue = "5") final long limit) {
        return cardService.findAllCards(page, limit);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/id")
    @ResponseStatus(OK)
    public Mono<CardResponse> findCardByCardId(@RequestParam(value = "cardId") @Id final String cardId) {
        return cardService.findCardByCardId(cardId);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/player")
    @ResponseStatus(OK)
    public Flux<CardResponse> findCardByPlayerId(@RequestParam(value = "playerId") @Id final String playerId) {
        return cardService.findCardByPlayerId(playerId);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/round")
    @ResponseStatus(OK)
    public Flux<CardResponse> findCardByRoundId(@RequestParam(value = "roundId") @Id final String roundId) {
        return cardService.findCardByRoundId(roundId);
    }
}
