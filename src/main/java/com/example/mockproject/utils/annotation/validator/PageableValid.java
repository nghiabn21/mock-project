package com.example.mockproject.utils.annotation.validator;

import com.example.mockproject.utils.annotation.PageableAnnotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PageableValid implements ConstraintValidator<PageableAnnotation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            int number = Integer.parseInt(value);
            if(number >= 0) {
                return true ;
            }
        }catch (Exception e) {
            return false ;
        }
        return false ;
    }
}
