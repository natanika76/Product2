package ru.natali.taskmanager.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String decodedAuth = "";

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length());
            decodedAuth = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
        }

        System.out.println("Request: " + request.getMethod() + " " + request.getRequestURI());
        System.out.println("Authorization Header: " + authHeader);
        System.out.println("Decoded Auth: " + decodedAuth);

        filterChain.doFilter(request, response);
    }
}
