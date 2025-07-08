package com.polymath.topay.dtos.request;


import com.polymath.topay.enums.Role;
import com.polymath.topay.validators.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 06 Sun Jul, 2025
 */
@Data
public class AddTeamMemberRequest {

    @NotNull(message = "Id cannot be null")
    @NotBlank(message = "Id cannot be blank")
    private UUID merchantId;

    @NotNull(message = "Admin Email cannot be null")
    @NotBlank(message = "Admin Email cannot be blank")
    @Email(message = "provide a valid email")
    private String adminEmail;
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "provide a valid email")
    private String email;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;
    private Role role;

    private String title;

}
