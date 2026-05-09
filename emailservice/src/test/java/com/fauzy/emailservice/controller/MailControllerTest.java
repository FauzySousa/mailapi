package com.fauzy.emailservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fauzy.emailservice.dto.EmailRequestDto;
import com.fauzy.emailservice.service.MailService;

@ExtendWith(MockitoExtension.class)
public class MailControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private MailService mailService;

    @InjectMocks
    private MailController mailController;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(mailController).build();
    }

    @Test
    @DisplayName("Deve retornar 202 quando requisição válida")
    void shouldReturnAcceptedWhenValidRequest() throws Exception {

        EmailRequestDto request = new EmailRequestDto(
            "test@email.com",
         "Assunto",
          "mensagem"
        );

        mockMvc.perform(post("/api/v1/emails/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());
            
        verify(mailService,times(1)).sendHtmlEmail(any());
                
    }
}
