package com.thangtranit.profileservice.controller;

import com.thangtranit.profileservice.dto.request.ChangeNameRequest;
import com.thangtranit.profileservice.dto.response.ApiResponse;
import com.thangtranit.profileservice.dto.response.ProfileMinimumResponse;
import com.thangtranit.profileservice.dto.response.ProfileResponse;
import com.thangtranit.profileservice.service.ProfileService;
import com.thangtranit.profileservice.validation.ValidateId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{id}")
    public ApiResponse<ProfileMinimumResponse> getProfile(
            @PathVariable String id,
            @RequestHeader(value = "uid", required = false) String uid
    ) {
        return ApiResponse.<ProfileMinimumResponse>builder()
                .body(profileService.getMinimum(id, uid))
                .build();
    }

    @GetMapping("/info/{id}")
    public ApiResponse<ProfileResponse> getProfileInfo(
            @PathVariable String id,
            @RequestHeader(value = "uid", required = false) String uid
    ) {
        return ApiResponse.<ProfileResponse>builder()
                .body(profileService.get(id, uid))
                .build();
    }

    @GetMapping("/profile_pictures/{id}")
    public ApiResponse<List<String>> getProfilePictures(@PathVariable String id) {
        return ApiResponse.<List<String>>builder()
                .body(profileService.getProfilePictureHistory(id))
                .build();
    }

    @GetMapping("/cover_pictures/{id}")
    public ApiResponse<List<String>> getCoverPictures(@PathVariable String id) {
        return ApiResponse.<List<String>>builder()
                .body(profileService.getCoverPictureHistory(id))
                .build();
    }

    @PatchMapping("/profile_picture")
    @ValidateId
    public ApiResponse<String> changeProfilePicture(
            MultipartFile file,
            @RequestHeader(name = "uid", required = false) String uid) {
        return ApiResponse.<String>builder()
                .body(profileService.changeProfilePicture(uid, file))
                .build();
    }

    @PatchMapping("/cover_picture")
    @ValidateId
    public ApiResponse<String> changeCoverPicture(
            MultipartFile file,
            @RequestHeader("uid") String uid) {
        return ApiResponse.<String>builder()
                .body(profileService.changeCoverPicture(uid, file))
                .build();
    }

    @PatchMapping("/name")
    @ValidateId
    public ApiResponse<Void> changeName(
            @RequestBody @Valid ChangeNameRequest request,
            @RequestHeader("uid") String uid) {
        profileService.changeName(uid, request);
        return new ApiResponse<>();
    }

    @PatchMapping("/birthdate")
    @ValidateId
    public ApiResponse<Void> changeBirthdate(
            @RequestHeader("uid") String uid,
            @RequestBody LocalDate birthdate) {
        profileService.changeBirthdate(uid, birthdate);
        return new ApiResponse<>();
    }

    @PatchMapping("/bio")
    @ValidateId
    public ApiResponse<Void> changeBio(
            @RequestBody(required = false) String bio,
            @RequestHeader("uid") String uid
    ) {
        profileService.changeBio(uid, bio);
        return new ApiResponse<>();
    }
}
