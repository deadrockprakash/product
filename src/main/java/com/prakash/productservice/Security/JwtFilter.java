package com.prakash.productservice.Security;

import com.prakash.productservice.entity.ROLE;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component

@Log4j2
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    @NullMarked
    protected void doFilterInternal(HttpServletRequest request,   HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestTokenHeader = request.getHeader("Authorization");
        log.info("Received request with Authorization header: {}", requestTokenHeader);
        String authToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            authToken = requestTokenHeader.substring(7);
        }
        if (authToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            Claims claims = jwtService.verifySignatureAndExtractClaims(authToken);

            ROLE role=  ROLE.valueOf(claims.get("role",String.class));
            log.info("Found role: {}", role);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>(List.of(new SimpleGrantedAuthority(role.name())));
            role.getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.name())));


            log.info("validateToken result: {}", jwtService.validateToken(authToken));
            if (jwtService.validateToken(authToken)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(claims.getSubject(),null, authorities);
                log.info(usernamePasswordAuthenticationToken);

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            } else{
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }

        }
        log.info("here");
        filterChain.doFilter(request, response);
    }
}
