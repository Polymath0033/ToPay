package com.polymath.topay.dtos.request;

import com.polymath.topay.validators.Email;
import com.polymath.topay.validators.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MerchantRegistrationRequest(@NotBlank(message = "Name is required") @NotNull(message = "Name cannot be null") String name, @NotBlank(message = "Email is required") @NotNull(message = "Email cannot be null") @Email(message = "provide a valid email") String email, @NotNull(message = "Password cannot be null") @NotBlank(message = "Password is required") @Password String password, String phone, String address) {
}
