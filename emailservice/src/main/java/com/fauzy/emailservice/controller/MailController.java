package com.fauzy.emailservice.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fauzy.emailservice.dto.ContactRequestDto;
import com.fauzy.emailservice.dto.EmailRequestDto;
import com.fauzy.emailservice.dto.EmailResponseDto;
import com.fauzy.emailservice.service.MailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
@Slf4j
@Tag(
    name = "Email Controller",
    description = "Responsável pelo envio de e-mails e formulários de contato"
)
public class MailController {

    private final MailService mailService;

    @Operation(
        summary = "Enviar e-mail HTML",
        description = "Endpoint responsável pelo envio de e-mails HTML"
    )
    @ApiResponse(
        responseCode = "202",
        description = "E-mail encaminhado para processamento"
    )
    @PostMapping("/send")
    public ResponseEntity<EmailResponseDto> sendEmail(@RequestBody @Valid EmailRequestDto request) {
        mailService.sendHtmlEmail(request);
        return ResponseEntity.accepted().body(
            new EmailResponseDto("E-mail encaminhado para processamento", LocalDateTime.now())
        );
    }

    @Operation(
        summary = "Processar formulário de contato",
        description = "Endpoint responsável pelo processamento de formulários de contato"
    )
    @ApiResponse(
        responseCode = "202",
        description = "Fomulário processado com suceso"
    )
    @PostMapping("/contact")
    public ResponseEntity<EmailResponseDto> contactForm(@RequestBody @Valid ContactRequestDto request) throws MessagingException {
        mailService.processContactForm(request);
        return ResponseEntity.accepted().body(
            new EmailResponseDto("Fomulário de contato recebido", LocalDateTime.now())
        );
    }
    
}
