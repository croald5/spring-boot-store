package com.codewithmosh.store.users;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import static java.util.Objects.*;

public class LowercaseValidator implements ConstraintValidator<Lowercase, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return isNull(value) || value.toLowerCase().equals(value);
    }
}
