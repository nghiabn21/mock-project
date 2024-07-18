package com.example.mockproject.utils.annotation.validator;

import com.example.mockproject.utils.annotation.ValueEnumConstraintCustomize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrdinalEnumValidatorCustomize implements ConstraintValidator<ValueEnumConstraintCustomize, String> {


    @Override
    public void initialize(ValueEnumConstraintCustomize constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value.equals("0") || value.equals("1") || value.equals("2")) {
            return true;
        }
        return false;
    }
}
