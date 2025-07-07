package com.polymath.topay.dtos.request;


import com.polymath.topay.validators.Email;
import com.polymath.topay.validators.Password;

/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 07 Mon Jul, 2025
 */

public record MerchantLoginRequest(@Email String email, @Password String password) {
}
