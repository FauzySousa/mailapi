package com.fauzy.emailservice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.springframework.test.util.ReflectionTestUtils;

import com.fauzy.emailservice.dto.EmailRequestDto;
import com.fauzy.emailservice.service.impl.MailServiceImpl;

import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
public class MailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;
    
    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private MailServiceImpl mailService;

    @BeforeEach
    void setup() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        ReflectionTestUtils.setField(mailService, "mailFrom", "test@email.com");
    }

    @Test
    void shouldSendEmailSuccessfully() {

        EmailRequestDto request = new EmailRequestDto(
            "test@email.com", 
            "Assunto",
            "<h1>Mensagem</h1>"
      );

      mailService.sendHtmlEmail(request);

      verify(mailSender,times(1)).send(any(MimeMessage.class));
    }

}
