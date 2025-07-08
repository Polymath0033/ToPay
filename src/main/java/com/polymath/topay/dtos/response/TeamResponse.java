package com.polymath.topay.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 08 Tue Jul, 2025
 */

@Data
@AllArgsConstructor
public class TeamResponse {
    private String name;
    private String email;
    private String title;
    private String role;
    private boolean isActive;
    private TokenResponse token;
    private MerchantResponse.ApiKeyResponse apiKey;

}
