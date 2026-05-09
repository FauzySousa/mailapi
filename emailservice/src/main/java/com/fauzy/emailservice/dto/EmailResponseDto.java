package com.fauzy.emailservice.dto;

import java.time.LocalDateTime;

public record EmailResponseDto(
    
    String message,
    LocalDateTime timestamp
    
) {}
