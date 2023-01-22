package com.bingo.domain.service;

import com.bingo.api.request.PlayerRequest;
import com.bingo.api.response.PageResponse;
import com.bingo.api.response.PlayerResponse;
import com.bingo.domain.document.PlayerDocument;
import com.bingo.domain.exception.PlayerAlreadyExistsException;
import com.bingo.domain.exception.PlayerNotFoundException;
import com.bingo.domain.repository.PlayerRepository;
import com.bingo.domain.service.query.PlayerQueryService;
import com.bingo.util.mapper.PlayerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerQueryService playerQueryService;
    private final PlayerMapper playerMapper;

    public Mono<PageResponse> findAllPlayers(final long page, final long limit) {
        return playerRepository.count()
                .zipWith(playerRepository.findAll()
                        .skip((page - 1) * limit)
                        .take(limit)
                        .map(playerMapper::toPlayerResponse)
                        .collectList())
                .map(zip -> PageResponse.builder()
                        .page(page)
                        .limit(limit)
                        .total(zip.getT1())
                        .itens(zip.getT2())
                        .build());
    }

    public Mono<PlayerResponse> findPlayer(final String playerId) {
        return playerQueryService.findPlayerByPlayerId(playerId)
                .map(playerMapper::toPlayerResponse);
    }

    public Mono<PlayerResponse> savePlayer(final PlayerRequest playerRequest) {
        return playerQueryService.findPlayerByPlayerEmail(playerRequest.email())
                .flatMap(player -> Mono.error(PlayerAlreadyExistsException::new))
                .onErrorResume(PlayerNotFoundException.class,
                        e -> playerRepository.save(playerMapper.toPlayerDocument(playerRequest)))
                .flatMap(player -> playerRepository.findByEmail(playerRequest.email()))
                .map(playerMapper::toPlayerResponse);
    }

    public Mono<PlayerResponse> updatePlayer(final String playerId, final PlayerRequest playerRequest) {
        return playerQueryService.findPlayerByPlayerId(playerId)
                .map(player -> PlayerDocument.builder()
                        .id(player.id())
                        .name(playerRequest.name())
                        .email(playerRequest.email())
                        .build())
                .flatMap(playerRepository::save)
                .map(playerMapper::toPlayerResponse);
    }

    public Mono<Void> deletePlayer(final String playerId) {
        return playerQueryService.findPlayerByPlayerId(playerId)
                .flatMap(playerRepository::delete);
    }
}
