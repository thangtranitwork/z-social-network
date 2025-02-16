package com.thangtranit.profileservice.entity;

import com.thangtranit.profileservice.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "profiles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile {
    @Id
    String id;
    String familyName;
    String givenName;
    @Builder.Default
    Gender gender = Gender.OTHER;
    LocalDate birthdate;
    String profilePicture;
    String coverPicture;
    String bio;
    LocalDate nextCanChangeNameDate;
    LocalDate nextCanChangeDOB;
    @Builder.Default
    List<String> profilePictureHistory = new ArrayList<>();
    @Builder.Default
    List<String> coverPictureHistory = new ArrayList<>();
    @Transient
    public static final int BIO_MAX_LENGTH = 255;
    @Transient
    public static final int NAME_MAX_LENGTH = 100;
    @Transient
    public static final int CHANGE_NAME_CD = 30;
    @Transient
    public static final int CHANGE_DOB_CD = 30;
    @Transient
    public static final int USER_MIN_AGE = 16;

    String userTrackingId;
}
