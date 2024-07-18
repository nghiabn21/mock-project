package com.example.mockproject.utils.handle_exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * This method use to check username/password attached to the request is it valid or not.
     * Handle information from request and send to response
     * This will send an error when the request to the path requires authentication (needs JWT string)
     * but does not include the JWT string
     * @param request that resulted in an <code>AuthenticationException</code>
     * @param response so that the user agent can begin authentication
     * @param e that caused the invocation
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Map<String, String> errors = new HashMap<>();
        errors.put("Status Code ", String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
        errors.put("Error ", e.getMessage());
        errors.put("Time", String.valueOf(LocalDateTime.now()));
        errors.put("Description", request.getServletPath());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), errors);
    }
}
