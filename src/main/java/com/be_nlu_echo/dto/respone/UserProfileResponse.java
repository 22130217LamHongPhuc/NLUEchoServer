package com.be_nlu_echo.dto.respone;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Builder
@ToString
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileResponse {
    private Long id;
    private String fullName;
    private String studentCode;
    private String avatarUrl;
    private String faculty;
    private String bio;
    private boolean defaultGhostMode;
}