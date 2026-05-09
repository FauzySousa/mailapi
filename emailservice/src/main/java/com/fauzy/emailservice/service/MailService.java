package com.fauzy.emailservice.service;

import com.fauzy.emailservice.dto.ContactRequestDto;
import com.fauzy.emailservice.dto.EmailRequestDto;



public interface MailService {
    void sendHtmlEmail(EmailRequestDto request);
    void processContactForm(ContactRequestDto contact);
}
