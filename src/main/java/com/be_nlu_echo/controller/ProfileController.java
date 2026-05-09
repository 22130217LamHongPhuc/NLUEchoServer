package com.be_nlu_echo.controller;


import com.be_nlu_echo.dto.request.UpdateProfileRequest;
import com.be_nlu_echo.dto.request.UpdateProfileResponse;
import com.be_nlu_echo.dto.respone.ApiResponse;
import com.be_nlu_echo.dto.respone.MyProfileResponse;
import com.be_nlu_echo.dto.respone.UserProfileResponse;
import com.be_nlu_echo.entity.User;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.service.CustomUserDetails;
import com.be_nlu_echo.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    UserProfileService userProfileService;



    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MyProfileResponse>> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
       MyProfileResponse profile =  userProfileService.getMyProfile(userDetails.getUserId());

       return ResponseEntity.ok(new ApiResponse<>(
               true,
               StatusCode.SUCCESS,
               "Profile retrieved successfully",
               profile
       ));
    }

    @PostMapping("/me")
    public ResponseEntity<ApiResponse<MyProfileResponse>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateProfileRequest request
            ) {
        System.out.println("Received update profile request: " + request);
        MyProfileResponse profile =  userProfileService.updateMyProfile(userDetails.getUserId(),request);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                StatusCode.SUCCESS,
                "Update profile successfully",
                profile
        ));


    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUseProfile(@PathVariable Long id){
        UserProfileResponse profile = userProfileService.getUserProfile(id);
        return ResponseEntity.ok(new ApiResponse<UserProfileResponse>(
                true,
                StatusCode.SUCCESS,
                "Profile retrieved successfully",
                profile
        ));
    }
}
