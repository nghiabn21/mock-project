package com.example.mockproject.utils.common;

import com.example.mockproject.dto.response.ErrorFilterDto;
import com.example.mockproject.dto.response.ErrorMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ResponseMessage {
    public static void handle(HttpServletRequest request, HttpServletResponse response, String message) {
            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, ErrorFilterDto> errors = new HashMap<>();
                errors.put("Error Message ",new ErrorFilterDto(message,
                        LocalDate.now(),
                        HttpStatus.NOT_FOUND,
                        request.getRequestURI()));
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

