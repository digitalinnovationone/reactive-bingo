package com.bingo.api.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record PlayerRequest(
        @NotBlank(message = "The name field is required")
        String name,
        @NotBlank(message = "The email field is required")
        @Email(message = "The email is invalid")
        String email) {
}
