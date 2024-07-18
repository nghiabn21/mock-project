package com.example.mockproject.filter;

import com.example.mockproject.dto.request.LoginDto;
import com.example.mockproject.model.User;
import com.example.mockproject.service.UserService;
import com.example.mockproject.utils.common.ResponseMessage;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.exception.ApiRequestException;
import com.example.mockproject.utils.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@AllArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken;
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
            LoginDto reds = new ObjectMapper().readValue(body, LoginDto.class);
            String account = reds.getAccount();
            String password = reds.getPassword();
            checkValid(account, password);
            account = account.trim();
            password = password.trim();
            User user = userService.findUserByAccount(account);
            if (user != null) {
                if (passwordEncoder.matches(password, user.getPassword())) {
                    authenticationToken = new UsernamePasswordAuthenticationToken(account, password);
                    return authenticationManager.authenticate(authenticationToken);
                } else if (passwordEncoder.matches(password, user.getTemporaryPassword())) {
                    User user1 = userService.checkPasswordUserExpiration(account);
                    if (user1 != null) {
                        authenticationToken = new UsernamePasswordAuthenticationToken(account, password);
                        return authenticationManager.authenticate(authenticationToken);
                    } else {
                        ResponseMessage.handle(request, response, "Token has expired");
                    }
                } else {
                    ResponseMessage.handle(request, response, Message.MESSAGE_002);
                }
            } else {
                ResponseMessage.handle(request, response, "Account is not existed");
            }
        } catch (IOException | ServiceException e) {
            log.info(e.getMessage());
            ResponseMessage.handle(request, response, e.getMessage());
        }
        return null;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String account;
        try {
            byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
            LoginDto reds = new ObjectMapper().readValue(body, LoginDto.class);
            account = reds.getAccount().trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        User user = userService.findUserByAccount(account);
        if (user.getStatus().getKey() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> errors = new HashMap<>();
            errors.put("Error Message ", "ACCOUNT IS NOT ACTIVE");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), errors);
        } else {
            request.setAttribute("auth", authentication);
            chain.doFilter(request, response);
        }
    }

    private void checkValid(String account, String password){
        List<ApiRequestException> list = new ArrayList<>();
        if (account == null) {
            list.add(new ApiRequestException("Account is should required"));
        } else if (account.trim().equals("")) {
            list.add(new ApiRequestException("Account is should required"));
        }
        if (password == null) {
            list.add(new ApiRequestException("Password is should required"));
        } else if (password.trim().equals("")) {
            list.add(new ApiRequestException("Password is should required"));
        }
        if (!list.isEmpty()) {
            throw new ServiceException(list);
        }
    }
}

