package com.example.mockproject.utils.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceException extends ApiRequestException{
    private List<ApiRequestException> exceptions;

    public ServiceException(String message, List<ApiRequestException> validationServiceExceptions) {
        super(message);
        if (validationServiceExceptions == null) {
            this.exceptions = null;
        } else {
            this.exceptions = new ArrayList<>(validationServiceExceptions);
        }
    }

    public ServiceException(List<ApiRequestException> validationServiceExceptions) {
        this(
                validationServiceExceptions != null ? toString(validationServiceExceptions) : null,
                validationServiceExceptions
        );
    }

    public List<ApiRequestException> getValidationServiceExceptions() {
        return exceptions;
    }

    private static String toString(List<ApiRequestException> validationServiceExceptions) {
        return validationServiceExceptions.stream()
                .map(cv -> cv == null ? "null" :  cv.getMessage())
                .collect(Collectors.joining(" and "));
    }

}
