package com.example.mockproject.utils.annotation.validator;

import com.example.mockproject.utils.annotation.PasswordValidation;
import com.example.mockproject.utils.constant.Message;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String > {
    public final static String PASSWORD_PATTERN ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    /**
     * Password at least
     * one lowercase character,
     * one uppercase character,
     * one digit,
     * one special character
     * and length between 8 to 20.
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
         return false ;
        }
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
