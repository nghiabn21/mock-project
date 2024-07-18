package com.example.mockproject.utils.handle_exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class MyAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * this method handling exception when user access link not authorization error 403
     * and show message notice to users
     * @param request
     * @param response
     * @param accessDeniedException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        if (!response.isCommitted()) {
            try {
                response.setHeader("Error", accessDeniedException.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String,String> errors = new HashMap<>() ;
                errors.put("Error Message ", accessDeniedException.getMessage());
                errors.put("Message ", "User is not authorized.");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

