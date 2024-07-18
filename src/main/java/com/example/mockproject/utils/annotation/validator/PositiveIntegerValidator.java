package com.example.mockproject.utils.annotation.validator;

import com.example.mockproject.utils.annotation.PositiveIntegerValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PositiveIntegerValidator implements ConstraintValidator<PositiveIntegerValidation, String> {
    /**
     * This method will check if the value from the user
     * does not accept values that are literal and less than 0
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     * @return boolean
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            int number = Integer.parseInt(value);
            if(number > 0) {
                return true ;
            }
        }catch (Exception e) {
        return false ;
        }
        return false ;
    }
}
