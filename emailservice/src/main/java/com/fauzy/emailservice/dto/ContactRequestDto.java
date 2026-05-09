package com.fauzy.emailservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactRequestDto(
    
    @NotBlank(message = "Nome é obrigatório")
    String name,

    @NotBlank(message = "O e-mail é origatório")
    @Email(message = "E-mail inválido")
    String senderEmail,

    @NotBlank(message = "A mensagem não pode estar vazia")
    String message
) {}
