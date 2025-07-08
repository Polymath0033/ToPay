package com.polymath.topay.controllers;


import com.polymath.topay.dtos.request.AddTeamMemberRequest;
import com.polymath.topay.dtos.request.MerchantLoginRequest;
import com.polymath.topay.dtos.response.ApiResponse;
import com.polymath.topay.dtos.response.TeamResponse;
import com.polymath.topay.enums.Role;
import com.polymath.topay.services.JWTService;
import com.polymath.topay.services.TeamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 07 Mon Jul, 2025
 */

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamsService teamsService;
    private final JWTService jWTService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeamResponse>> addTeamMember(@RequestBody AddTeamMemberRequest request){
        TeamResponse response = teamsService.addTeamMember(request);
        return new ResponseEntity<>(ApiResponse.success(HttpStatus.CREATED.value(),"added a team member",response),HttpStatus.CREATED);
    }

    @PatchMapping("/change-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeamResponse>> changeTeamRole(@RequestBody Role request, @RequestBody String teamMemberEmail, @RequestBody String adminEmail,@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String merchantEmail = jWTService.extractEmailFromToken(token);
        teamsService.updateTeamMemberRole(request,adminEmail,merchantEmail,teamMemberEmail);
        return new ResponseEntity<>(ApiResponse.success(HttpStatus.OK.value(),"Successfully changed role",null),HttpStatus.OK);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<TeamResponse>> changeTeamPassword(@RequestBody MerchantLoginRequest request){
        teamsService.updateTeamMemberPassword(request);
        return new ResponseEntity<>(ApiResponse.success(HttpStatus.OK.value(),"Successfully changed password",null),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeamMember(@PathVariable String id, @RequestBody AddTeamMemberRequest request){
       TeamResponse response =  teamsService.updateTeamMember(id,request);
        return new ResponseEntity<>(ApiResponse.success(HttpStatus.OK.value(),"Successfully changed password",response),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteTeamMember(@PathVariable Long id,@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String merchantEmail = jWTService.extractEmailFromToken(token);
        teamsService.deleteTeamMember(id,merchantEmail);
        return new ResponseEntity<>(ApiResponse.success(HttpStatus.OK.value(),"Successfully deleted team member",null),HttpStatus.OK);
    }

}
