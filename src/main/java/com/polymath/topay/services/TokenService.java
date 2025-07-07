package com.polymath.topay.services;


import com.polymath.topay.dtos.response.TokenResponse;
import com.polymath.topay.exceptions.CustomBadRequest;
import com.polymath.topay.exceptions.CustomNotFound;
import com.polymath.topay.models.Teams;
import com.polymath.topay.models.Token;
import com.polymath.topay.repositories.MerchantsRepository;
import com.polymath.topay.repositories.TeamRepository;
import com.polymath.topay.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 07 Mon Jul, 2025
 */

@Service
@RequiredArgsConstructor
public class TokenService {
    private final  JWTService jwtService;
    private final TeamRepository teamRepository;
    private final TokenRepository tokenRepository;

    private boolean isTokenValid(String token){
        Token existingToken = tokenRepository.findByToken(token).orElseThrow(()->new CustomNotFound("Token not found"));
        return !existingToken.isRevoked() && existingToken.getExpiresAt().isAfter(LocalDateTime.now());
    }

    public TokenResponse getToken(String accessToken){
        String email = jwtService.extractEmailFromToken(accessToken);
        Token token = tokenRepository.findByTeamEmail(email);
        if(!isTokenValid(token.getToken())){
            throw new CustomBadRequest("Token is expired");
        }

        return buildTokenResponse(accessToken,token.getToken(),token.isRevoked());
    }

    public TokenResponse generateToken(String email, Long teamId, UUID merchantId,String role){
        if(email==null){
            throw new CustomBadRequest("Email cannot be null");
        }
        String accessToken = jwtService.generateAccessToken(email,teamId,merchantId, role);
        String refreshToken = jwtService.generateRefreshToken(email,teamId,merchantId, role);
        Teams teams = teamRepository.findByEmail(email).orElseThrow();
        if(tokenRepository.existsByTeamEmail(email)){
            Token existingToken = tokenRepository.findByTeamEmail(email);
            existingToken.setToken(refreshToken);
            existingToken.setIssuedAt(LocalDateTime.now());
            existingToken.setExpiresAt(LocalDateTime.now().plusDays(7));
            existingToken.setRevoked(false);
            existingToken.setTeam(teams);
            tokenRepository.save(existingToken);
        }else {
            Token token = new Token();
            token.setToken(refreshToken);
            token.setIssuedAt(LocalDateTime.now());
            token.setExpiresAt(LocalDateTime.now().plusDays(7));
            token.setRevoked(false);
            token.setTeam(teams);
            tokenRepository.save(token);
        }
        return buildTokenResponse(accessToken,refreshToken,false);

    }

    private TokenResponse buildTokenResponse(String accessToken,String refreshToken,boolean revoked){
        LocalDateTime refreshTokenExpiresAt = jwtService.extractExpirationDate(refreshToken);
        LocalDateTime accessTokenExpiresAt = jwtService.extractExpirationDate(accessToken);
        return new TokenResponse(refreshToken,accessToken,localDateTimeToLong(refreshTokenExpiresAt),localDateTimeToLong(accessTokenExpiresAt),revoked);

    }

    private long localDateTimeToLong(LocalDateTime dateTime){
        return Math.max(0, dateTime.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }
}
