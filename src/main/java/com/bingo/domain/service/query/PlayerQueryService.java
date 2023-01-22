package com.bingo.domain.service.query;

import com.bingo.domain.document.PlayerDocument;
import com.bingo.domain.exception.PlayerNotFoundException;
import com.bingo.domain.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerQueryService {
    private final PlayerRepository playerRepository;

    public Mono<PlayerDocument> findPlayerByPlayerId(final String playerId) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(PlayerNotFoundException::new)));
    }

    public Mono<PlayerDocument> findPlayerByPlayerEmail(final String playerEmail) {
        return playerRepository.findByEmail(playerEmail)
                .switchIfEmpty(Mono.defer(() -> Mono.error(PlayerNotFoundException::new)));
    }
}
