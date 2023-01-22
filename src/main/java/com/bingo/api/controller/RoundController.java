package com.bingo.api.controller;

import com.bingo.api.request.CardRequest;
import com.bingo.api.response.CardResponse;
import com.bingo.api.response.PageResponse;
import com.bingo.api.response.RoundResponse;
import com.bingo.domain.service.RoundService;
import com.bingo.util.validator.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import java.util.LinkedHashSet;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/round")
@RequiredArgsConstructor
@Validated
public class RoundController {
    private final RoundService roundService;

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/all")
    @ResponseStatus(OK)
    public Mono<PageResponse> findAllRounds(
            @RequestParam(value = "page", required = false, defaultValue = "1") final long page,
            @RequestParam(value = "limit", required = false, defaultValue = "5") final long limit) {
        return roundService.findAllRounds(page, limit);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/id")
    @ResponseStatus(OK)
    public Mono<RoundResponse> findRound(@RequestParam(value = "roundId") @Id final String roundId) {
        return roundService.findRound(roundId);
    }

    @PostMapping(path = "/new-round")
    public Mono<RoundResponse> createRound() {
        return roundService.createRound();
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/winning-numbers")
    @ResponseStatus(OK)
    public Mono<LinkedHashSet<Integer>> findWinningNumbers(@RequestParam(value = "roundId") @Id final String roundId) {
        return roundService.findWinningNumbers(roundId);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/last-number")
    @ResponseStatus(OK)
    public Mono<Integer> findLastNumber(@RequestParam(value = "roundId") @Id final String roundId) {
        return roundService.findLastNumber(roundId);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/new-number")
    @ResponseStatus(CREATED)
    public Mono<Integer> findNewNumber(@RequestParam(value = "roundId") @Id final String roundId) {
        return roundService.findNewNumber(roundId);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, path = "/add-player")
    @ResponseStatus(CREATED)
    public Mono<CardResponse> enrollPlayer(@RequestBody @Valid final CardRequest cardRequest) {
        return roundService.enrollPlayer(cardRequest);
    }
}
