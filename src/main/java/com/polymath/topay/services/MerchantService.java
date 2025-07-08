package com.polymath.topay.services;


import com.polymath.topay.dtos.request.MerchantRegistrationRequest;
import com.polymath.topay.dtos.response.MerchantResponse;
import com.polymath.topay.dtos.response.TokenResponse;
import com.polymath.topay.enums.Role;
import com.polymath.topay.exceptions.CustomBadRequest;
import com.polymath.topay.exceptions.CustomNotFound;
import com.polymath.topay.models.Merchants;
import com.polymath.topay.models.Teams;
import com.polymath.topay.repositories.MerchantsRepository;
import com.polymath.topay.repositories.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 07 Mon Jul, 2025
 */

@Service
@RequiredArgsConstructor
public class MerchantService {
    private final TeamRepository teamRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final String DEFAULT_PASSWORD = "12345";
    private final TokenService tokenService;
    private final MerchantsRepository merchantsRepository;
    private final ApiKeyService apiKeyService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public MerchantResponse registerMerchant(MerchantRegistrationRequest request){
        boolean existByEmail = teamRepository.existsByEmail(request.email());
        if(existByEmail){
            throw new CustomBadRequest("Email already exist");
        }
        Merchants merchants = new Merchants();
        merchants.setEmail(request.email());
        merchants.setPassword(encoder.encode(request.password()));
        merchants.setPhone(request.phone());
        merchants.setAddress(request.address());
        merchants.setName(request.name());
        merchants.setCreatedAt(LocalDateTime.now());
        merchants.setUpdatedAt(LocalDateTime.now());
        merchants.setActive(true);
        merchantsRepository.save(merchants);
        apiKeyService.generateApiKeyForMerchant(merchants.getId());
        Teams teams = new Teams();
        teams.setName(request.name());
        teams.setEmail(request.email());
        teams.setPassword(encoder.encode(request.password()));
        teams.setRole(Role.ADMIN);
        teams.setCreatedAt(LocalDateTime.now());
        teams.setUpdatedAt(LocalDateTime.now());
        teams.setMerchants(merchants);
        teamRepository.save(teams);
        TokenResponse token = tokenService.generateToken(request.email(),teams.getId(),merchants.getId(),teams.getRole().name());
        List<MerchantResponse.Team> teamsList = getTeams(merchants.getId());
        MerchantResponse.ApiKeyResponse apiKeys = new MerchantResponse.ApiKeyResponse(merchants.getTestApiKey().getPublicKey(),merchants.getTestApiKey().getSecretKey());
        return new MerchantResponse(merchants.getId(),merchants.getName(),merchants.getEmail(),merchants.getPhone(),merchants.getAddress(),teams.getRole().name(), teamsList,merchants.isActive(),apiKeys,token);
    }

    @Transactional
    public MerchantResponse loginMerchant(String email,String password){
        try {
            Teams teams = teamRepository.findByEmail(email).orElseThrow(()->new CustomNotFound("Email not found"));
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
            if(authentication.isAuthenticated()){
                String role = authentication.getAuthorities().stream().findFirst().orElseThrow().getAuthority();
                TokenResponse token = tokenService.generateToken(email,teams.getId(),teams.getMerchants().getId(),role);
                List<MerchantResponse.Team> team = getTeams(teams.getMerchants().getId());
                MerchantResponse.ApiKeyResponse apiKey = new MerchantResponse.ApiKeyResponse(teams.getMerchants().getTestApiKey().getPublicKey(),teams.getMerchants().getTestApiKey().getSecretKey());
                return new MerchantResponse(teams.getMerchants().getId(),teams.getMerchants().getName(),teams.getMerchants().getEmail(),teams.getMerchants().getPhone(),teams.getMerchants().getAddress(),role, team,teams.getMerchants().isActive(),apiKey,token);
            }else{
                return new MerchantResponse(null,null,null,null,null,null,null,false,null,null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private List<MerchantResponse.Team> getTeams(UUID merchantId){
        List<Teams> teamsList =  teamRepository.findByMerchantsId(merchantId);
        return teamsList.stream().map(team1 -> new MerchantResponse.Team(team1.getName(),team1.getEmail(),team1.getTitle(),team1.getRole(),team1.isActive())).toList();
    }
}
