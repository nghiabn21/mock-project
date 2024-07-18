package com.example.mockproject.utils.annotation;

import com.example.mockproject.utils.annotation.validator.DateValid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE, ElementType.LOCAL_VARIABLE})
@Constraint(validatedBy = DateValid.class)
public @interface DateAnnotation {
    /**
     * @return the error message template
     */
    String message() default "Date is not true";

    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default {};
}
