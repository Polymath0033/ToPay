package com.polymath.topay.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<Email,String> {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");
    private boolean allowEmpty;
    private String[] blockedDomains;
    private boolean requireTld;

    @Override
    public void initialize(Email constraintAnnotation) {
        this.allowEmpty = constraintAnnotation.allowEmpty();
        this.blockedDomains = constraintAnnotation.blockedDomains();
        this.requireTld = constraintAnnotation.requireTld();
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.trim().isEmpty()){
            return allowEmpty;
        }

        if(!EMAIL_PATTERN.matcher(value).matches()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid email format").addConstraintViolation();
            return false;
        }

        if(blockedDomains != null && blockedDomains.length > 0){
            String domain = value.substring(value.indexOf("@")+1).toLowerCase();
            if(Arrays.asList(blockedDomains).contains(domain)){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Email domain is blocked").addConstraintViolation();
                return false;
            }
        }

        if(requireTld && !value.contains(".")){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Email must contain a domain").addConstraintViolation();
            return false;
        }
        return true;
    }
}
