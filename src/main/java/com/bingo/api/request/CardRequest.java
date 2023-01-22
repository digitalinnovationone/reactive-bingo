package com.bingo.api.request;

import com.bingo.util.validator.Id;

import javax.validation.constraints.NotBlank;

public record CardRequest(
        @NotBlank(message = "The userId field is required")
        @Id
        String playerId,
        @NotBlank(message = "The roundId field is required")
        @Id
        String roundId) {
}
