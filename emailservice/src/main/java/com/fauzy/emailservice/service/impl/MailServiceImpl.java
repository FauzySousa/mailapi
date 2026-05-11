package com.fauzy.emailservice.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fauzy.emailservice.dto.ContactRequestDto;
import com.fauzy.emailservice.dto.EmailRequestDto;
import com.fauzy.emailservice.exception.EmailSendingException;
import com.fauzy.emailservice.service.MailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    // Busca a variável de ambiente ou usa o padrão do Resend para testes
    @Value("${spring.mail.from:onboarding@resend.dev}")
    private String mailFrom;

    @Async("mailExecutor")
    @Retryable(
        retryFor = {EmailSendingException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @Override
    public void sendHtmlEmail(EmailRequestDto request) {

        log.info("Iniciando envio de e-mail via SMTP Resend para: {}", request.to());
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            
            // O 'true' indica que é um e-mail multipart (necessário para HTML)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailFrom);
            helper.setTo(request.to());
            helper.setSubject(request.subject());
            helper.setText(request.body(), true); // 'true' habilita a renderização HTML

            mailSender.send(message);

            log.info("E-mail enviado com sucesso para: {}", request.to());

        } catch (MessagingException | MailException ex) {
            log.error("Erro técnico ao tentar conectar ou enviar para {}: {}", request.to(), ex.getMessage());
            
            // Lançamos a nossa Exception customizada para disparar o @Retryable
            throw new EmailSendingException("Falha no transporte do e-mail", ex);
        }
    }

    @Override
    public void processContactForm(ContactRequestDto contact) {

        log.info("Processando novo formulário de contato de: {}", contact.name());

        String htmlContent = buildContactTemplate(contact);

        // Criamos o DTO de envio usando o mailFrom autorizado (onboarding@resend.dev)
        EmailRequestDto emailRequest = new EmailRequestDto(
            mailFrom, 
            "Novo Contato: " + contact.name(),
            htmlContent
        );

        sendHtmlEmail(emailRequest);
    }

    private String buildContactTemplate(ContactRequestDto contact) {
        Context context = new Context();
        context.setVariable("name", contact.name());
        context.setVariable("email", contact.senderEmail());
        context.setVariable("message", contact.message());

        return templateEngine.process("contact-template", context);
    }

    @Recover
    public void recover(EmailSendingException ex, EmailRequestDto request) {
        log.error("""
            
            ===== FALHA CRÍTICA APÓS TENTATIVAS DE REENVIO =====
            
            Data/Hora: {}
            Destinatário: {}
            Assunto: {}
            Causa da Falha: {}
            
            ====================================================
            """,
            LocalDateTime.now(),
            request.to(),
            request.subject(),
            ex.getMessage()
        );
    }
}