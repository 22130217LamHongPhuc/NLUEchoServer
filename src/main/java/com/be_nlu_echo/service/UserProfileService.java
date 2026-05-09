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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;

    public MyProfileResponse getMyProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(
                        "User not found",
                        StatusCode.USER_NOT_FOUND
                ));

        return mapToMyProfileResponse(user);
    }

    @Transactional
    public MyProfileResponse updateMyProfile(
            Long userId,
            UpdateProfileRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(request.getFullName().isEmpty()){
            throw new AppException("Full name and student code cannot be empty", StatusCode.INVALID_INPUT);
        }

        if(request.getFullName().equals(user.getFullName()) && request.getStudentCode().equals(user.getStudentCode())
                && request.getFaculty().equals(user.getFaculty()) && request.getBio().equals(user.getBio())
                && request.getAvatarUrl().equals(user.getAvatarUrl())){
            return mapToMyProfileResponse(user);
        }

        user.setFullName(request.getFullName());
        user.setFaculty(request.getFaculty());
        user.setBio(request.getBio());
        user.setStudentCode(request.getStudentCode());
        user.setAvatarUrl(request.getAvatarUrl());

        userRepository.save(user);

        return mapToMyProfileResponse(user);
    }

    public MyProfileResponse mapToMyProfileResponse(User user) {
        return MyProfileResponse.builder()
                .id(user.getId())
                .studentCode(user.getStudentCode())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .faculty(user.getFaculty())
                .bio(user.getBio())
                .emailVerified(user.isEmailVerified())
                .defaultGhostMode(user.isDefaultGhostMode())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
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