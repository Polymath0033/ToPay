package com.polymath.topay.controllers;


import com.polymath.topay.dtos.request.MerchantLoginRequest;
import com.polymath.topay.dtos.request.MerchantRegistrationRequest;
import com.polymath.topay.dtos.response.ApiResponse;
import com.polymath.topay.dtos.response.MerchantResponse;
import com.polymath.topay.services.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 06 Sun Jul, 2025
 */

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<MerchantResponse>> registerMerchant(@RequestBody MerchantRegistrationRequest request){
        MerchantResponse merchantResponse = merchantService.registerMerchant(request);
        return new ResponseEntity<>(ApiResponse.success(HttpStatus.OK.value(),"Merchant registered successfully",merchantResponse),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MerchantResponse>> loginMerchant(@RequestBody MerchantLoginRequest request){
        MerchantResponse merchantResponse = merchantService.loginMerchant(request.email(),request.password());
        return new ResponseEntity<>(ApiResponse.success(HttpStatus.OK.value(),"Merchant logged in successfully",merchantResponse),HttpStatus.OK);
    }
}
