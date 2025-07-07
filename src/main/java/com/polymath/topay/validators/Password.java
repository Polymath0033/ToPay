package com.polymath.topay.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {
    String message() default "Invalid password format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int minLength() default 8;
    int maxLength() default 128;
    boolean requireDigit() default true;
    boolean requireLowerCase() default true;
    boolean requireUpperCase() default true;
    String specialCharacters() default "!@#$%^&*()_+-[]{}|;:,.<>?";
    boolean requireSpecialCharacters() default true;
    boolean allowWhitespace() default false;
    String[] commonPasswords() default {};
}
