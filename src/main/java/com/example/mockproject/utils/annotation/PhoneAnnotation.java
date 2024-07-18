package com.example.mockproject.utils.annotation;

import com.example.mockproject.utils.annotation.validator.PhoneValidate;
import com.example.mockproject.utils.constant.Message;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy= PhoneValidate.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneAnnotation {
    String message() default Message.MESSAGE_027;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
