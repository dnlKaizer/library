package br.cefetmg.library.controller.dto;

import java.time.Instant;

public record LoginResponse(
    String token,
    Instant expiresAt,
    String login,
    String role
) {
}