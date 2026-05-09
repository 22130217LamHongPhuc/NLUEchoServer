package com.be_nlu_echo.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileResponse {
    String fullName;
    String faculty;
    String bio;
    String avatarUrl;
    String studentCode;
}
