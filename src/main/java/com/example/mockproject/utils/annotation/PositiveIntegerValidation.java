package com.example.mockproject.utils.annotation;

import com.example.mockproject.utils.annotation.validator.PositiveIntegerValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy= PositiveIntegerValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveIntegerValidation {
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
