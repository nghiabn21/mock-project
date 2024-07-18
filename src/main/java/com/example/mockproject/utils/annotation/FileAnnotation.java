package com.example.mockproject.utils.annotation;

import com.example.mockproject.utils.annotation.validator.FileValidation;
import com.example.mockproject.utils.annotation.validator.OrdinalEnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = FileValidation.class)
public @interface FileAnnotation {
    String message() default "file must be the format .pdf,.doc,.docx";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
