package com.polymath.topay.exceptions;


/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 07 Mon Jul, 2025
 */

public class CustomNotAuthorized extends RuntimeException {
    public CustomNotAuthorized(String message) {
        super(message);
    }
}
