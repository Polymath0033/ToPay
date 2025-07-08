package com.polymath.topay.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polymath.topay.services.JWTService;
import com.polymath.topay.services.MyUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JWTFilter  extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final MyUserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
        try{
            if(authHeader != null && authHeader.startsWith("Bearer ")){
                token = authHeader.substring(7);
                email = jwtService.extractEmailFromToken(token);
                if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    if(jwtService.isTokenValid(token)){
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }else {
                        throw new BadCredentialsException("Invalid token");
                    }
                }
            }
        }catch (ExpiredJwtException ex){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
            responseBody.put("message", "Authentication expired, please log in again");
            responseBody.put("data", null);

            new ObjectMapper().writeValue(response.getOutputStream(), responseBody);

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        filterChain.doFilter(request,response);

    }
}
