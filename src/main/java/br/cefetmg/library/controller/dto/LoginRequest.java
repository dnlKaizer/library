package br.cefetmg.library.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "O usuario e obrigatorio.")
    String username,
    @NotBlank(message = "A senha e obrigatoria.")
    String password
) {
}