package com.example.assignment.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author Madhusudhan S
 * Jun-2023
 */

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String BEARER ="Bearer " ;
    @Autowired
    private JwtTokenUtil jwtUtil;


    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String requestURI = request.getRequestURI();

        if (requestURI.contains("/swagger-ui") || requestURI.contains("/api/auth/authenticate") ||
                requestURI.contains("/v3/api-docs") || requestURI.contains("/swagger-resources")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            // Return error response if authorization header is missing or doesn't start with Bearer
            sendAuthErrorResponse(response);
            return;
        }

        final String jwtToken = authHeader.replace(BEARER, "");
        try {
            jwtUtil.validateToken(jwtToken);  // Validate token
            String subject = jwtUtil.getUsernameFromToken(jwtToken);

            if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String role = jwtUtil.getRoleFromToken(jwtToken);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        subject, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception ex) {
            // Token validation failed, return error response
            sendAuthErrorResponse(response);
            return;
        }

        // Continue with the filter chain if authentication is successful
        filterChain.doFilter(request, response);
    }


    private void sendAuthErrorResponse(HttpServletResponse response) throws IOException {
        String json = objectMapper.writeValueAsString("Invalid or missing token, provide a valid token in the Authorisation header with \"Bearer \" prefix");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.getWriter().write(json);
    }



}
