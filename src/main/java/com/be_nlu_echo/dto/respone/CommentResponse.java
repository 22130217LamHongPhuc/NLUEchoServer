package com.be_nlu_echo.dto.respone;

import com.be_nlu_echo.dto.respone.UserResponse;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long id;

    private String content;

    private UserResponse user;

    private LocalDateTime createdAt;
}