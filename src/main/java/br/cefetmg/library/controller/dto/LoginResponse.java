package br.cefetmg.library.controller.dto;

public record LoginResponse(
    String token,
    String login,
    String role
) {}