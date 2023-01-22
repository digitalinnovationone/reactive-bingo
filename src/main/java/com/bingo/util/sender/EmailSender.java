package com.bingo.util.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class EmailSender {
    private final JavaMailSender javaMailSender;

    public Mono<Boolean> sendEmail(final String from, final String to, final String subject, final String text) {
        return Mono.just(new SimpleMailMessage())
                .map(message -> {
                    message.setFrom(from);
                    message.setTo(to);
                    message.setSubject(subject);
                    message.setText(text);
                    return message;})
                .flatMap(message -> Mono.defer(() -> {
                    try {
                        javaMailSender.send(message);
                    } catch (MailException e) {
                        return Mono.just(false);
                    }
                    return Mono.just(true);
                }))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
