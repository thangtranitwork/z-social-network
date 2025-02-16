package com.thangtranit.profileservice.mapper;

import com.thangtranit.profileservice.dto.request.ProfileCreationRequest;
import com.thangtranit.profileservice.dto.response.ProfileMinimumResponse;
import com.thangtranit.profileservice.dto.response.ProfileResponse;
import com.thangtranit.profileservice.entity.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(ProfileCreationRequest profileCreationRequest);
    ProfileResponse toProfileResponse(Profile profile);
    ProfileMinimumResponse toProfileMinimumResponse(Profile profile);
}
