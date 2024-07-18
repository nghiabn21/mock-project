package com.example.mockproject.utils.annotation;

import com.example.mockproject.utils.annotation.validator.IntegerListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IntegerListValidator.class})
@Documented
public @interface ValidIntegerList {
    String message() default "Invalid integer list!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
