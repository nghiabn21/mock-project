package com.example.mockproject.utils.annotation.validator;

import com.example.mockproject.utils.annotation.ValueEnumConstraint;
import com.example.mockproject.utils.annotation.YearOfExperience;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class YearOfExperienceValid implements ConstraintValidator<YearOfExperience, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        try {
            int number = Integer.parseInt(value);
            if (number >= 0 && number <= 30) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
