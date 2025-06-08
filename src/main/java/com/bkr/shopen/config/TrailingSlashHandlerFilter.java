package com.bkr.shopen.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.lang.NonNull;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TrailingSlashHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith("/") && requestURI.length() > 1) {
            String newURI = requestURI.substring(0, requestURI.length() - 1);
            String queryString = request.getQueryString();
            if (queryString != null) {
                newURI += "?" + queryString;
            }
            response.sendRedirect(newURI);
            return;
        }
        filterChain.doFilter(request, response);
    }
}