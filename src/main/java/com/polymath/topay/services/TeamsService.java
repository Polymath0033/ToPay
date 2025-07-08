package com.polymath.topay.services;


import com.polymath.topay.dtos.request.AddTeamMemberRequest;
import com.polymath.topay.dtos.request.MerchantLoginRequest;
import com.polymath.topay.dtos.request.MerchantRegistrationRequest;
import com.polymath.topay.dtos.response.MerchantResponse;
import com.polymath.topay.dtos.response.TeamResponse;
import com.polymath.topay.dtos.response.TokenResponse;
import com.polymath.topay.enums.Role;
import com.polymath.topay.exceptions.CustomBadRequest;
import com.polymath.topay.exceptions.CustomNotAuthorized;
import com.polymath.topay.exceptions.CustomNotFound;
import com.polymath.topay.models.Merchants;
import com.polymath.topay.models.Teams;
import com.polymath.topay.repositories.MerchantsRepository;
import com.polymath.topay.repositories.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 06 Sun Jul, 2025
 */

@Service
@RequiredArgsConstructor
public class TeamsService {

    private final TeamRepository teamRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final MerchantsRepository merchantsRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public TeamResponse addTeamMember(AddTeamMemberRequest request){
        Merchants merchants = merchantsRepository.findById(request.getMerchantId()).orElseThrow(()->new CustomNotFound("Merchant not found"));
        Teams teams = teamRepository.findByEmail(request.getAdminEmail()).orElseThrow(()->new CustomNotFound("Email not found"));
        if(teams.getRole() != Role.ADMIN){
            throw new CustomBadRequest("Only admin can add team member");
        }

        if(teamRepository.existsByEmail(request.getEmail())){
            throw new CustomBadRequest("Email already exist");
        }
        Teams team = new Teams();
        team.setName(request.getName());
        String DEFAULT_PASSWORD = "12345";
        team.setPassword(encoder.encode(DEFAULT_PASSWORD));
        team.setEmail(request.getEmail());
        if(request.getRole()==null){
            team.setRole(Role.MEMBER);
        }else {
            team.setRole(request.getRole());
        }
        team.setCreatedAt(LocalDateTime.now());
        team.setUpdatedAt(LocalDateTime.now());
        team.setMerchants(merchants);
        teamRepository.save(team);
        merchants.getTeams().add(team);
        merchantsRepository.save(merchants);
        TokenResponse token = tokenService.generateToken(request.getEmail(),teams.getId(),merchants.getId(),teams.getRole().name());
        MerchantResponse.ApiKeyResponse apiKey = new MerchantResponse.ApiKeyResponse(merchants.getTestApiKey().getPublicKey(),merchants.getTestApiKey().getSecretKey());
        return new TeamResponse(request.getName(),request.getEmail(),request.getTitle(),teams.getRole().name(),teams.isActive(),token,apiKey);
    }

    @Transactional
    public void updateTeamMemberPassword(MerchantLoginRequest request){
       Teams team = teamRepository.findByEmail(request.email()).orElseThrow(()->new CustomNotFound("Email not found"));
       team.setPassword(encoder.encode(request.password()));
       team.setUpdatedAt(LocalDateTime.now());
       teamRepository.save(team);

    }

    @Transactional
    public TeamResponse updateTeamMember(String id, AddTeamMemberRequest request){
        Teams teams = teamRepository.findByEmail(request.getEmail()).orElseThrow(()->new CustomNotFound("Email not found"));
        teams.setName(request.getName());
        teams.setTitle(request.getTitle());
        teams.setUpdatedAt(LocalDateTime.now());
        teamRepository.save(teams);
        Merchants merchants = merchantsRepository.findById(teams.getMerchants().getId()).orElseThrow(()->new CustomNotFound("Merchant not found"));
        MerchantResponse.ApiKeyResponse apiKey = new MerchantResponse.ApiKeyResponse(merchants.getTestApiKey().getPublicKey(),merchants.getTestApiKey().getSecretKey());
        return new TeamResponse(request.getName(),teams.getEmail(),teams.getTitle(),teams.getRole().name(),teams.isActive(),null,apiKey);
    }

    @Transactional
    public void updateTeamMemberRole(Role role,String adminEmail,String merchantEmail, String teamMemberEmail){
       merchantsRepository.findByEmail(merchantEmail).orElseThrow(()->new CustomNotAuthorized("Invalid Authorization token"));
       Teams adminMember = teamRepository.findByEmail(adminEmail).orElseThrow(()->new CustomNotFound("Team member not found"));
       if(adminMember.getRole()!=Role.ADMIN){
           throw new CustomBadRequest("Only admin can update team member role");
       }
       Teams teamMember = teamRepository.findByEmail(teamMemberEmail).orElseThrow(()->new CustomNotFound("Team member not found"));
       teamMember.setRole(role);
       teamMember.setUpdatedAt(LocalDateTime.now());
       teamRepository.save(teamMember);
    }

    @Transactional
    public void deleteTeamMember(Long teamId,String adminEmail){
        Teams adminMember = teamRepository.findByEmail(adminEmail).orElseThrow(()->new CustomNotFound("Team member not found"));
        if(adminMember.getRole()!=Role.ADMIN){
            throw new CustomNotAuthorized("Only admin can delete team member");
        }
        teamRepository.deleteById(teamId);
    }




}
