package com.bingo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EmailConfig {
    @Value(value = "${config.email.from}")
    private String from;
    @Value(value = "${config.email.subject}")
    private String subject;
    @Value(value = "${config.email.winner-text}")
    private String winnerText;
    @Value(value = "${config.email.loser-text}")
    private String loserText;
}
