package com.polymath.topay.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class PasswordValidator implements ConstraintValidator<Password,String> {

    int minLength;
    int maxLength;
    boolean requireDigit;
    boolean requireLowerCase;
    boolean requireUpperCase;
    String specialCharacters;
    boolean requireSpecialCharacters;
    boolean allowWhitespace;
    List<String> commonPasswords;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.requireDigit = constraintAnnotation.requireDigit();
        this.requireLowerCase = constraintAnnotation.requireLowerCase();
        this.requireUpperCase = constraintAnnotation.requireUpperCase();
        this.specialCharacters = constraintAnnotation.specialCharacters();
        this.requireSpecialCharacters = constraintAnnotation.requireSpecialCharacters();
        this.allowWhitespace = constraintAnnotation.allowWhitespace();
        this.commonPasswords = Arrays.asList(constraintAnnotation.commonPasswords());

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.trim().isEmpty()){
            return false;
        }

        if(value.length() < minLength || value.length() > maxLength){
            addViolation(context,"Password length must be between "+minLength+" and "+maxLength);
            return false;
        }

        if(requireDigit && !value.matches(".*\\d+.*")){
            addViolation(context,"Password must contain at least one digit");
            return false;
        }

        if(requireLowerCase && !value.matches(".*[a-z]+.*")){
            addViolation(context,"Password must contain at least one lowercase letter");
            return false;
        }

        if(requireUpperCase && !value.matches(".*[A-Z]+.*")){
            addViolation(context,"Password must contain at least one uppercase letter");
            return false;
        }

        if(requireSpecialCharacters && value.chars().noneMatch(c -> specialCharacters.indexOf(c) != -1)){
            addViolation(context,"Password must contain at least one special character");
            return false;
        }
        if(!allowWhitespace && value.contains(" ")){
            addViolation(context,"Password cannot contain whitespace");
            return false;
        }

        if(!commonPasswords.isEmpty()&&commonPasswords.contains(value.toLowerCase())){
            addViolation(context,"Password is a common password");
        }
        //        if(requireSpecialCharacters && !value.matches(".*["+specialCharacters+"]+.*")){
        //            addViolation(context,"Password must contain at least one special character");
        //            return false;
        //        }
        return true;
    }
    private void addViolation(ConstraintValidatorContext context, String message){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
