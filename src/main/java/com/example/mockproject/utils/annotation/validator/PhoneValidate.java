package com.example.mockproject.utils.annotation.validator;

import com.example.mockproject.utils.annotation.PhoneAnnotation;
import com.example.mockproject.utils.validation.Validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidate implements ConstraintValidator<PhoneAnnotation, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value==null || value.isEmpty()){
            return true;
        }else {
          return Pattern.compile(Validation.PHONE_PATTERN).matcher(value).find();
        }

    }
}
