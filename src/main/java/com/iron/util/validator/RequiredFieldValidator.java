package com.iron.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;

public class RequiredFieldValidator implements ConstraintValidator<RequiredField, Object> {
    @Override
    public void initialize(RequiredField constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value instanceof String str) {
            return !str.trim().isEmpty();
        }

        if (value instanceof Collection<?> col) {
            if (col.isEmpty()) return false;
            // проверяем, что все элементы не null и не пустые строки
            for (Object item : col) {
                if (item == null) return false;
                if (item instanceof String s && s.trim().isEmpty()) return false;
            }
        }

        return true;
    }
}
