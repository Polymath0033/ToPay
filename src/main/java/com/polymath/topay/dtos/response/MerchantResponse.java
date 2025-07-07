package com.polymath.topay.dtos.response;


import com.polymath.topay.enums.Role;
import com.polymath.topay.models.ApiKey;
import com.polymath.topay.models.Teams;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 07 Mon Jul, 2025
 */

@Data
@AllArgsConstructor
public class MerchantResponse {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
    private List<Team> teams;
    private boolean isActive;
    private ApiKeyResponse apiKey;
    private TokenResponse token;

    @Data
    @AllArgsConstructor
    public static class Team{
        private String name;
        private String email;
        private String title;
        private Role role;
        private boolean isActive;
    }
    @Data
    @AllArgsConstructor
    public static class ApiKeyResponse{
        private String publicKey;
        private String secretKey;
    }
}
