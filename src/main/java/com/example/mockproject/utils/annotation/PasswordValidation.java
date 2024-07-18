package com.example.mockproject.utils.annotation;

import com.example.mockproject.utils.annotation.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy= PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValidation {
    String message() default "Password Invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}