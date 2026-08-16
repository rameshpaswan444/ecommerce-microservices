package com.order_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalServiceFilter extends OncePerRequestFilter {

    private final String internalSecret;

    public InternalServiceFilter(
            @Value("${internal.service.secret}") String internalSecret) {

        this.internalSecret = internalSecret;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String secret =
                request.getHeader("X-Internal-Service");

        if (internalSecret.equals(secret)) {

            request.setAttribute(
                    "INTERNAL_SERVICE_AUTHENTICATED",
                    true
            );
        }

        filterChain.doFilter(request, response);
    }
}