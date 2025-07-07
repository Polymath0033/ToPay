package com.polymath.topay.dtos.response;


import com.polymath.topay.models.Token;

/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 07 Mon Jul, 2025
 */

public record TokenResponse(String refreshToken,String accessToken,Long refreshTokenExpiresIn,long accessTokenExpiresIn,boolean revoked) {
}
