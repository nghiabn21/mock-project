package com.example.mockproject.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.mockproject.utils.common.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Component
@Slf4j
public class CustomHeaderAuthFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Authentication authentication = (Authentication) request.getAttribute("auth");
        if (authentication == null) {
            filterChain.doFilter(request, response);
        } else {
            User user = (User) authentication.getPrincipal();
            // header of JWT
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            var roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            // payload of JWT
            String access_token = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                    .withIssuer(request.getRequestURI())
                    .withClaim("roles", roles)
                    .sign(algorithm);
            String refresh_token = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                    .withIssuer(request.getRequestURI())
                    .sign(algorithm);
                response.setHeader("access_token", access_token);
                response.setHeader("refresh_token", refresh_token);
                filterChain.doFilter(request, response);
        }
    }
}
