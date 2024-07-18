package com.example.mockproject.utils.handle_exception;


import com.example.mockproject.dto.response.ErrorMessageDto;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.exception.ApiNotFoundException;
import com.example.mockproject.utils.exception.ApiRequestException;
import com.example.mockproject.utils.exception.ServiceException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * This method will catch error about bean validation
     * and will report that error to the user
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, ErrorMessageDto> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            ErrorMessageDto message = new ErrorMessageDto(error.getDefaultMessage(),
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST,
                    request.getDescription(false));
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    /**
     * This method will catch error ApiRequestException
     * and will report that error to the user
     *
     * @param e
     * @param request
     * @return ResponseEntity
     */
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e, WebRequest request) {
        Map<String, ErrorMessageDto> result = new HashMap<>();
        result.put("Error Message: ", new ErrorMessageDto(e.getMessage(),
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                request.getDescription(false)));
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method will catch error ConstraintViolationException
     * and will report that error to the user
     *
     * @param exception
     * @param request
     * @return ResponseEntity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handle(ConstraintViolationException exception, WebRequest request) {
        List<ErrorMessageDto> errors = new ArrayList<>();
        Map<String, List<ErrorMessageDto>> result = new HashMap<>();
        exception.getConstraintViolations().forEach(cv -> errors.add(new ErrorMessageDto(cv.getMessage(),
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                request.getDescription(false))));
        result.put("Error Message: ", errors);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method will catch error ApiNotFoundException
     * and will report that error to the user
     *
     * @param exception
     * @param request
     * @return ResponseEntity
     */
    @ExceptionHandler({ApiNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleApiNotFoundException(ApiNotFoundException exception, WebRequest request) {
        Map<String, ErrorMessageDto> result = new HashMap<>();
        result.put("Error Message: ", new ErrorMessageDto(exception.getMessage(),
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                request.getDescription(false)));
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method will catch error ServiceException in Filter
     * and will report that error to the user
     *
     * @param e
     * @param request
     * @return ResponseEntity
     */
    @ExceptionHandler(value = {ServiceException.class})
    public ResponseEntity<Object> handleServiceException(ServiceException e, WebRequest request) {
        Map<String, ErrorMessageDto> result = new HashMap<>();
        e.getValidationServiceExceptions().forEach(err -> {
            result.put("Error Message: ", new ErrorMessageDto(e.getMessage(),
                    LocalDateTime.now(),
                    HttpStatus.NOT_FOUND,
                    request.getDescription(false)));
        });
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method will handle when user don't input data
     * @param e the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, ErrorMessageDto> result = new HashMap<>();
        result.put("Error Message: ",new ErrorMessageDto(Message.MESSAGE_089,
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                request.getDescription(false)));
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

}

