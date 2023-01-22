package com.bingo.domain.service;

import com.bingo.config.EmailConfig;
import com.bingo.domain.exception.RoundNotFinishedException;
import com.bingo.domain.repository.PlayerRepository;
import com.bingo.domain.service.query.CardQueryService;
import com.bingo.domain.service.query.RoundQueryService;
import com.bingo.util.sender.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.LinkedHashSet;

import static com.bingo.util.generator.CardGenerator.HITS_TO_WIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final PlayerRepository playerRepository;
    private final CardQueryService cardQueryService;
    private final RoundQueryService roundQueryService;
    private final EmailConfig emailConfig;
    private final EmailSender emailSender;

    public Flux<Boolean> sendEmailToRoundPlayers(final String roundId) {
        return roundQueryService.findWinningsNumbersByRoundId(roundId)
                .flatMapMany(winning -> cardQueryService.findCardByRoundId(roundId)
                        .flatMap(card -> playerRepository.findById(card.playerId())
                                .map(player -> Tuples.of(winning, card.numbers(), player.email()))))

                .switchIfEmpty(Mono.defer(() -> Mono.error(RoundNotFinishedException::new)))

                .map(tuple -> {
                    LinkedHashSet<Integer> hits = new LinkedHashSet<>(tuple.getT2());
                    hits.retainAll(tuple.getT1());
                    boolean winner = hits.size() >= HITS_TO_WIN;
                    return Tuples.of(tuple.getT1(), winner, tuple.getT3());
                })

                .flatMap(tuple -> {
                    String text = tuple.getT2() ? emailConfig.getWinnerText() : emailConfig.getLoserText();
                    return emailSender.sendEmail(
                            emailConfig.getFrom(),
                            tuple.getT3(),
                            emailConfig.getSubject(),
                            String.format("%s %s\n", text, tuple.getT1())
                    );
                });
    }
}
