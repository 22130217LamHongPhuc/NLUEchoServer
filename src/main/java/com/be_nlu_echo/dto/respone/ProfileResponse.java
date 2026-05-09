package com.be_nlu_echo.dto.respone;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private Long id;
    private String studentCode;
    private String fullName;
    private String email;
    private String avatarUrl;
    private String faculty;
    private String bio;
    private boolean emailVerified;
    private boolean defaultGhostMode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
