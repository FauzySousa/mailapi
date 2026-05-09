package com.fauzy.emailservice.exception;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta padrão de erro da API")
public record ErrorResponse(

    @Schema(
        description = "Codigo HTTP do erro",
        example = "400"
    )
    int status,

    @Schema(
        description = "Tipo do erro",
        example = "Validation Error"
    )
    String error,

    @Schema(
        description = "Mensagem detalhada do erro",
        example = "O e-mail é obrigatório"
    )
    String message,

    @Schema(
        description = "Endpoint onde ocorreu o erro",
        example = "/api/v1/email/send"
    )
    String path,

    @Schema(
        description = "Data e hora do erro",
        example = "2026-05-11T13:30:00"
    )
    LocalDateTime timestamp
) {}
