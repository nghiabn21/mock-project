package com.example.mockproject.utils.annotation.validator;

import com.example.mockproject.utils.annotation.DoubleAnotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DoubleValidation implements ConstraintValidator<DoubleAnotation, String> {

    public static final String DOUBLE_POSITIVE = "[0-9]{1,13}(\\.[0-9]*)?";

    /**
     * Check validation of Double
     * Input "", null, or digit, character .
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if ("".equals(value) || value == null) {
            return true;
        }
        Pattern pattern = Pattern.compile(DOUBLE_POSITIVE);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

}
