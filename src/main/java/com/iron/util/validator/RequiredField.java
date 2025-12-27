package com.iron.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {RequiredFieldValidator.class})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredField {
    String message() default "Field is required";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
