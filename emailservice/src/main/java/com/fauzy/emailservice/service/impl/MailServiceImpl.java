package com.fauzy.emailservice.service.impl;

import com.fauzy.emailservice.dto.ContactRequestDto;
import com.fauzy.emailservice.dto.EmailRequestDto;
import com.fauzy.emailservice.exception.EmailSendingException;
import com.fauzy.emailservice.service.MailService;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final TemplateEngine templateEngine;

    // Pega a API Key (re_...) das variáveis da Railway
    @Value("${resend.api.key}")
    private String resendApiKey;

    // Pega o e-mail de origem (onboarding@resend.dev)
    @Value("${resend.from}")
    private String mailFrom;

    @Async("mailExecutor")
    @Retryable(
        retryFor = {EmailSendingException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @Override
    public void sendHtmlEmail(EmailRequestDto request) {
        log.info("Iniciando envio via API HTTP do Resend para: {}", request.to());

        // Inicializa o cliente com a Key
        Resend resend = new Resend(resendApiKey);

        // Monta os parâmetros da chamada HTTP
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(mailFrom)
                .to(request.to())
                .subject(request.subject())
                .html(request.body())
                .build();

        try {
            // Chamada de rede via HTTPS (Porta 443)
            CreateEmailResponse response = resend.emails().send(params);
            log.info("E-mail enviado com sucesso via API! ID: {}", response.getId());

        } catch (ResendException e) {
            log.error("Erro na API do Resend ao enviar para {}: {}", request.to(), e.getMessage());
            // Lançamos a exception para o Retryable tentar novamente se necessário
            throw new EmailSendingException("Falha na chamada da API do Resend", e);
        }
    }

    @Override
    public void processContactForm(ContactRequestDto contact) {
        log.info("Processando formulário de contato de: {}", contact.name());

        String htmlContent = buildContactTemplate(contact);

        
        EmailRequestDto emailRequest = new EmailRequestDto(
            "fauzydev9@gmail.com", 
            "Novo Contato: " + contact.name(),
            htmlContent
        );

        sendHtmlEmail(emailRequest);
    }

    private String buildContactTemplate(ContactRequestDto contact) {
        Context context = new Context();
        context.setVariable("name", contact.name());
        context.setVariable("email", contact.senderEmail());
        context.setVariable("phone", contact.phone());
        context.setVariable("message", contact.message());

        return templateEngine.process("contact-template", context);
    }

    @Recover
    public void recover(EmailSendingException ex, EmailRequestDto request) {
        log.error("""
            
            ===== FALHA CRÍTICA APÓS TENTATIVAS (API RESEND) =====
            Data/Hora: {}
            Destinatário: {}
            Causa: {}
            =======================================================
            """,
            LocalDateTime.now(),
            request.to(),
            ex.getMessage()
        );
    }
}