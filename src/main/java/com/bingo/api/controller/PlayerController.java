package com.bingo.api.controller;

import com.bingo.api.request.PlayerRequest;
import com.bingo.api.response.PageResponse;
import com.bingo.api.response.PlayerResponse;
import com.bingo.domain.service.PlayerService;
import com.bingo.util.validator.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
@Validated
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/all")
    @ResponseStatus(OK)
    public Mono<PageResponse> findAllPlayers(
            @RequestParam(value = "page", required = false, defaultValue = "1") final long page,
            @RequestParam(value = "limit", required = false, defaultValue = "5") final long limit) {
        return playerService.findAllPlayers(page, limit);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/id")
    @ResponseStatus(OK)
    public Mono<PlayerResponse> findPlayer(@RequestParam(value = "playerId") @Id final String playerId) {
        return playerService.findPlayer(playerId);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<PlayerResponse> savePlayer(@RequestBody @Valid final PlayerRequest playerRequest) {
        return playerService.savePlayer(playerRequest);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Mono<PlayerResponse> updatePlayer(
            @RequestParam(value = "playerId") @Id final String playerId,
            @RequestBody @Valid final PlayerRequest playerRequest) {
        return playerService.updatePlayer(playerId, playerRequest);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> deletePlayer(@RequestParam(value = "playerId") @Id final String playerId) {
        return playerService.deletePlayer(playerId);
    }
}
