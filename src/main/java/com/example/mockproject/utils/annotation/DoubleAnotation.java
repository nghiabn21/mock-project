package com.example.mockproject.utils.annotation;

import com.example.mockproject.utils.annotation.validator.DoubleValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DoubleValidation.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleAnotation {
    String message() default "Number is not format correct";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
