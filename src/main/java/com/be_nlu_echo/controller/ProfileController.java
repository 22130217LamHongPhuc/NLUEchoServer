package com.be_nlu_echo.controller;


import com.be_nlu_echo.dto.respone.ApiResponse;
import com.be_nlu_echo.dto.respone.MyProfileResponse;
import com.be_nlu_echo.dto.respone.UserProfileResponse;
import com.be_nlu_echo.entity.User;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    UserProfileService userProfileService;



    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MyProfileResponse>> getProfile(Authentication authentication) {
       MyProfileResponse profile =  userProfileService.getMyProfile(authentication);

       return ResponseEntity.ok(new ApiResponse<>(
               true,
               StatusCode.SUCCESS,
               "Profile retrieved successfully",
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
