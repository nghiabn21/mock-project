package com.example.mockproject.utils.annotation.validator;

import com.example.mockproject.utils.annotation.DateAnnotation;
import com.example.mockproject.utils.constant.DateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Annotation checks that the date is in the correct format
 */
public class DateValid implements ConstraintValidator<DateAnnotation, String> {

    public static SimpleDateFormat dtoFormat = new SimpleDateFormat(DateFormat.DATETIME);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if ("".equals(value) || value == null) {
            return true;
        }
        dtoFormat.setLenient(false);
        try {
            dtoFormat.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
