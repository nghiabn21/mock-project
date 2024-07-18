package com.example.mockproject.dto.response;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageDto {
    private String message ;
    private LocalDateTime localDate ;
    private HttpStatus status ;
    private String description ;
}
