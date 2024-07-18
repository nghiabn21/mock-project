package com.example.mockproject.utils.annotation.validator;

import com.example.mockproject.utils.annotation.OrdinalEnumConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrdinalEnumValidator implements ConstraintValidator<OrdinalEnumConstraint, CharSequence> {
    private List<String> acceptedValues = new ArrayList<>();

    @Override
    public void initialize(OrdinalEnumConstraint annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(anEnum -> String.valueOf(anEnum.ordinal()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null || value.equals("")) {
            return true;
        }

        return acceptedValues.contains(value.toString());
    }
}
