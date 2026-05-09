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

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Async("mailExecutor")
    @Retryable(retryFor = {EmailSendingException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @Override
    public void sendHtmlEmail(EmailRequestDto request) {

            log.info("enviando o e-mail para {}", request.to());
           
            try{

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(mailFrom);
                helper.setTo(request.to());
                helper.setSubject(request.subject());
                helper.setText(request.body(), true);

                mailSender.send(message);

                log.info("E-mail enviado com sucesso para: {}", request.to());

            } catch(MessagingException | MailException ex) {

                log.error("Erro ao enviar email para {}", request.to(), ex);

                throw new EmailSendingException("Falha ao enviar email", ex);
            }
    }

    @Override
    public void processContactForm(ContactRequestDto contact) {

        log.info("Processando formulário de contato de {}", contact.name());

        String htmlContent = buildContactTemplate(contact);

        EmailRequestDto request = new EmailRequestDto(
            mailFrom,
            "Novo Contato: " + contact.name(),
            htmlContent
        );

        sendHtmlEmail(request);
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
            
            ===== FALHA DEFINITIVA NO ENVIO DE E-MAIL =====
            
            Timestamp: {}
            Destinatário: {}
            Assunto: {}
            Tipo do erro: {}
            Mensagem: {}
            
            ===============================================
            """,
            LocalDateTime.now(),
            request.to(),
            request.subject(),
            ex.getClass().getSimpleName(),
            ex.getMessage()
        );
        
    }

}
