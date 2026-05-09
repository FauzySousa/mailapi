package com.fauzy.emailservice.exception;

public class EmailSendingException extends RuntimeException{

    public EmailSendingException(String message) {
        super(message);
    }

    public EmailSendingException(String messege, Throwable cause){
        super(messege,cause);
    }
}
