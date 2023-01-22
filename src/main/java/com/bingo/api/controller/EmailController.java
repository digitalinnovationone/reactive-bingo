package com.bingo.api.controller;

import com.bingo.domain.service.EmailService;
import com.bingo.util.validator.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Validated
public class EmailController {
    private final EmailService emailService;

    @PostMapping(path = "/round")
    @ResponseStatus(OK)
    public Flux<Boolean> sendEmailToRoundPlayers(@RequestParam(value = "roundId") @Id final String roundId) {
        return emailService.sendEmailToRoundPlayers(roundId);
    }
}
