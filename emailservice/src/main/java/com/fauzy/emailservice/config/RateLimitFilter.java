package com.fauzy.emailservice.config;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter extends OncePerRequestFilter{

    private static final int LIMIT = 5;
    private static final Duration DURATION = Duration.ofMinutes(1);

    private final Map<String,Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        
        return Bucket.builder()
                .addLimit(limit -> limit
                    .capacity(LIMIT)
                    .refillGreedy(LIMIT, DURATION)
                )
                .build();
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
                String path = request.getRequestURI();

                if (!path.equals("api/v1/emails/contact")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String ip = request.getRemoteAddr();

                Bucket bucket = cache.computeIfAbsent(
                    ip, 
                    k -> createNewBucket()
                );

                if (!bucket.tryConsume(1)) {
                    
                    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    response.addHeader("X-Rate-Limit-Limit",String.valueOf(LIMIT));
                    response.addHeader("X-Rate-Limit-Remaining", "0");

                    response.getWriter().write("""
                        {
                            "error": "Limite de requisições excedido. Tente novamente em 1 minuto."
                     }""");

                     return;
                }

                filterChain.doFilter(request, response);
    }

}
