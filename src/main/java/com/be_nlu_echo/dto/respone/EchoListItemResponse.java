package com.be_nlu_echo.dto.respone;

import com.be_nlu_echo.enums.EchoType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EchoListItemResponse {
    private Long id;
    private String title;
    private String contentPreview;
    private EchoType echoType;
    private boolean anonymous;
    private String authorName;
    private String authorAvatarUrl;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createdAt;
}

