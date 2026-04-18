package com.be_nlu_echo.service;

import com.be_nlu_echo.dto.request.UpdateProfileRequest;
import com.be_nlu_echo.dto.respone.MyProfileResponse;
import com.be_nlu_echo.dto.respone.UserProfileResponse;
import com.be_nlu_echo.entity.User;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.exception.AppException;
import com.be_nlu_echo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;

    public MyProfileResponse getMyProfile(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        assert userDetails != null;
        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new AppException(
                        "User not found",
                        StatusCode.USER_NOT_FOUND
                ));

        return mapToMyProfileResponse(user);
    }

    @Transactional
    public MyProfileResponse updateMyProfile(
            Authentication authentication,
            UpdateProfileRequest request
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        assert userDetails != null;
        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(request.getFullName());
        user.setFaculty(request.getFaculty());
        user.setBio(request.getBio());

        return mapToMyProfileResponse(user);
    }

    private MyProfileResponse mapToMyProfileResponse(User user) {

        MyProfileResponse profile =  MyProfileResponse.builder()
                .id(user.getId())
                .studentCode(user.getStudentCode())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .faculty(user.getFaculty())
                .bio(user.getBio())
                .email(user.getEmail())
                .build();
        return profile;
    }

    private UserProfileResponse mapToUserProfileResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .studentCode(user.getStudentCode())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .faculty(user.getFaculty())
                .bio(user.getBio())
                .build();
    }

    public UserProfileResponse getUserProfile(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new AppException("User not found with id: " + id,
                        StatusCode.USER_NOT_FOUND));
        return mapToUserProfileResponse(user);
    }
}