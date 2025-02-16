package com.thangtranit.profileservice.service;

import com.thangtranit.profileservice.dto.request.ChangeNameRequest;
import com.thangtranit.profileservice.dto.request.ProfileCreationRequest;
import com.thangtranit.profileservice.dto.response.ApiResponse;
import com.thangtranit.profileservice.dto.response.ProfileMinimumResponse;
import com.thangtranit.profileservice.dto.response.ProfileResponse;
import com.thangtranit.profileservice.entity.Profile;
import com.thangtranit.profileservice.entity.UserTracking;
import com.thangtranit.profileservice.enums.FileType;
import com.thangtranit.profileservice.enums.Privacy;
import com.thangtranit.profileservice.exception.AppException;
import com.thangtranit.profileservice.exception.ErrorCode;
import com.thangtranit.profileservice.mapper.ProfileMapper;
import com.thangtranit.profileservice.repository.ProfileRepository;
import com.thangtranit.profileservice.repository.UserTrackingRepository;
import com.thangtranit.profileservice.repository.client.FileClient;
import com.thangtranit.profileservice.repository.client.RelationshipClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final FileClient fileClient;
    private final RelationshipClient relationshipClient;
    private final JsonConverter jsonConverter;
    private final UserTrackingRepository userTrackingRepository;

    public void createProfile(ProfileCreationRequest request) {
        if (profileRepository.existsById(request.id()))
            throw new AppException(ErrorCode.PROFILE_ALREADY_EXISTS);

        if (request.birthDate() != null && Period.between(LocalDate.now(), request.birthDate()).getYears() >= Profile.USER_MIN_AGE)
            throw new AppException(ErrorCode.USER_MUST_BE_OVER_16_YEARS_OLD);

        Profile profile = profileMapper.toProfile(request);
        UserTracking userTracking = new UserTracking();
        userTrackingRepository.save(userTracking);
        profile.setUserTrackingId(userTracking.getId());
        profileRepository.save(profile);
    }

    public ProfileResponse get(String targetId, String uid) {
        if (uid != null && !uid.isEmpty() && !targetId.equals(uid)) {
            validateBlock(targetId);
        }
        Profile profile = getProfile(targetId);
        userTrackingRepository.findById(profile.getUserTrackingId())
                .ifPresent(userTracking -> userTracking.view(targetId));
        return profileMapper.toProfileResponse(profile);
    }

    public ProfileMinimumResponse getMinimum(String id, String uid) {
        if (uid != null && !uid.isEmpty() && !id.equals(uid)) {
            validateBlock(id);
        }
        return profileMapper.toProfileMinimumResponse(getProfile(id));
    }

    public List<String> getProfilePictureHistory(String id) {
        return getProfile(id).getProfilePictureHistory();
    }

    public List<String> getCoverPictureHistory(String id) {
        return getProfile(id).getCoverPictureHistory();
    }

    public String changeProfilePicture(String uid, MultipartFile file) {
        Profile profile = getProfile(uid);
        try {
            ApiResponse<String> response = fileClient.uploadFile(
                    file,
                    Privacy.PUBLIC,
                    profile.getId(),
                    new HashSet<>(),
                    new HashSet<>(),
                    FileType.IMAGE);
            profile.getProfilePictureHistory().add(profile.getProfilePicture());
            profile.setProfilePicture(response.getBody());
            profileRepository.save(profile);
            return response.getBody();
        } catch (FeignException.FeignClientException e) {
            throw new AppException(
                    ErrorCode.UPLOAD_PROFILE_PICTURE_FAILED,
                    Map.of("detail", jsonConverter.convertJsonToMap(e.contentUTF8()))
            );
        }
    }

    public String changeCoverPicture(String uid, MultipartFile file) {
        Profile profile = getProfile(uid);
        try {
            ApiResponse<String> response = fileClient.uploadFile(
                    file, Privacy.PUBLIC,
                    profile.getId(),
                    new HashSet<>(),
                    new HashSet<>(),
                    FileType.IMAGE);
            profile.getCoverPictureHistory().add(profile.getCoverPicture());
            profile.setCoverPicture(response.getBody());
            profileRepository.save(profile);
            return response.getBody();
        } catch (FeignException.FeignClientException e) {
            throw new AppException(
                    ErrorCode.UPLOAD_COVER_PICTURE_FAILED,
                    Map.of("detail", jsonConverter.convertJsonToMap(e.contentUTF8()))
            );
        }
    }

    public void changeName(String uid, ChangeNameRequest request) {
        Profile profile = getProfile(uid);
        if (profile.getNextCanChangeNameDate() != null &&
                profile.getNextCanChangeNameDate().isAfter(LocalDate.now())) {
            throw new AppException(ErrorCode.CHANGE_NAME_FAILED_BECAUSE_IN_COUNT_DOWN, Map.of("date", profile.getNextCanChangeNameDate()));
        }

        profile.setGivenName(request.givenName());
        profile.setFamilyName(request.familyName());
        profile.setNextCanChangeNameDate(LocalDate.now().plusDays(Profile.CHANGE_NAME_CD));
        profileRepository.save(profile);
    }

    public void changeBirthdate(String uid, LocalDate birthdate) {
        Profile profile = getProfile(uid);
        if (profile.getNextCanChangeDOB() != null &&
                profile.getNextCanChangeDOB().isAfter(LocalDate.now())) {
            throw new AppException(ErrorCode.CHANGE_BIRTH_DATE_FAILED_BECAUSE_IN_COUNT_DOWN, Map.of("date", profile.getNextCanChangeDOB()));
        }

        if (birthdate == null)
            throw new AppException(ErrorCode.BIRTHDATE_IS_REQUIRED);
        if (Period.between(LocalDate.now(), birthdate).getYears() >= Profile.USER_MIN_AGE)
            throw new AppException(ErrorCode.USER_MUST_BE_OVER_16_YEARS_OLD);

        profile.setBirthdate(birthdate);
        profile.setNextCanChangeDOB(LocalDate.now().plusDays(Profile.CHANGE_DOB_CD));
        profileRepository.save(profile);
    }


    private Profile getProfile(String request) {
        return profileRepository.findById(request)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
    }

    private void validateBlock(String id) {
        ApiResponse<Boolean> response = relationshipClient.isBlocked(id);
        if (Boolean.TRUE.equals(response.getBody())) {
            throw new AppException(ErrorCode.BLOCKED);
        }

    }

    public void changeBio(String uid, String bio) {
        Profile profile = getProfile(uid);
        if (bio == null) {
            profile.setBio("");
        } else {
            profile.setBio(bio.length() > Profile.BIO_MAX_LENGTH ? bio.substring(0, Profile.BIO_MAX_LENGTH) : bio);
        }
        profileRepository.save(profile);
    }
}
