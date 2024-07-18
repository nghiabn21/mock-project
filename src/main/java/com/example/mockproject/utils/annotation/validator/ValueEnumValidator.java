package com.example.mockproject.utils.annotation.validator;

import com.example.mockproject.utils.annotation.ValueEnumConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueEnumValidator implements ConstraintValidator<ValueEnumConstraint, CharSequence> {
    private List<String> acceptedValues = new ArrayList<>();

    @Override
    public void initialize(ValueEnumConstraint annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value.toString().toUpperCase());
    }
}
