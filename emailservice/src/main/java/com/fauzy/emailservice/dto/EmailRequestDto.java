package com.fauzy.emailservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequestDto(
    
    @Schema(
        description = "E-mail destinatário",
        example = "teste@gmail.com"
    )
    @NotBlank(message = "O destinatário é obrigatorio")
    @Email(message = "E-mail inválido")
    String to,

    @Schema(
        description = "Assunto do e-mail",
        example = "Bem-vindo"
    )
    @NotBlank(message = "O assunto é obrigatório")
    String subject,

    @Schema(
        description = "Conteúdo HTML do e-mail",
        example = "<h1>Olá</h1>"
    )
    @NotBlank(message = "O corpo da mensagem é Obrigatório")
    String body
) {}
