package com.polymath.topay.exceptions;

public class CustomBadRequest extends RuntimeException {
    public CustomBadRequest(String message) {
        super(message);
    }
}
