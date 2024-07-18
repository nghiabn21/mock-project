package com.example.mockproject.utils.annotation.validator;

import com.example.mockproject.utils.annotation.IntegerValidation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IntegerValidator implements ConstraintValidator<IntegerValidation, String> {
    /**
     * This method will check if the value from the user
     * does not accept values that are literal and less than 0
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     * @return boolean
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
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
