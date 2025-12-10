package com.cosmocats.market.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final String headerName;
    private final String apiKeyValue;
    private final String protectedPathPattern;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public ApiKeyAuthenticationFilter(
            @Value("${security.api-key.header-name}") String headerName,
            @Value("${security.api-key.value}") String apiKeyValue,
            @Value("${security.api-key.paths:/api/v1/products/**}") String protectedPathPattern
    ) {
        this.headerName = headerName;
        this.apiKeyValue = apiKeyValue;
        this.protectedPathPattern = protectedPathPattern;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !pathMatcher.match(protectedPathPattern, path);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        Authentication existing = SecurityContextHolder.getContext().getAuthentication();
        if (existing == null || !existing.isAuthenticated()) {
            String apiKey = request.getHeader(headerName);

            if (apiKey != null && apiKey.equals(apiKeyValue)) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                "api-key-client",
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_API_CLIENT"))
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
