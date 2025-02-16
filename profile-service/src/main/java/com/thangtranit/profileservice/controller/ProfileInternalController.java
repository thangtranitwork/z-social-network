package com.thangtranit.profileservice.controller;

import com.thangtranit.profileservice.dto.request.ProfileCreationRequest;
import com.thangtranit.profileservice.dto.response.ApiResponse;
import com.thangtranit.profileservice.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class ProfileInternalController {
    private final ProfileService profileService;

    @PostMapping
    public ApiResponse<Void> createProfile(@RequestBody @Valid ProfileCreationRequest request) {
        profileService.createProfile(request);
        return new ApiResponse<>();
    }
}
