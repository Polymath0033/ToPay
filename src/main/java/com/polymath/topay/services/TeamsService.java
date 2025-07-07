package com.polymath.topay.services;


import com.polymath.topay.dtos.request.AddTeamMemberRequest;
import com.polymath.topay.dtos.request.MerchantRegistrationRequest;
import com.polymath.topay.enums.Role;
import com.polymath.topay.exceptions.CustomBadRequest;
import com.polymath.topay.models.Merchants;
import com.polymath.topay.models.Teams;
import com.polymath.topay.repositories.MerchantsRepository;
import com.polymath.topay.repositories.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final String DEFAULT_PASSWORD = "12345";
    private final MerchantsRepository merchantsRepository;
    private final TokenService tokenService;

    @Transactional
    public void registerTeam(MerchantRegistrationRequest request){

    }


    public void addTeamMember(AddTeamMemberRequest request){

    }

}
