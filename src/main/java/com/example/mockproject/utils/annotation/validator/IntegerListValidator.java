package com.example.mockproject.utils.annotation.validator;

import com.example.mockproject.utils.annotation.ValidIntegerList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class IntegerListValidator implements ConstraintValidator<ValidIntegerList, List<String>> {
    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        try {
            value.forEach(n -> Integer.parseInt(n));
        }catch (Exception e) {
            return false;
        }
        return true;
    }
}
