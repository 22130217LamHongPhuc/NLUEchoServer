package com.be_nlu_echo.dto.respone;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class EchoDetailResponse {
    Long id;
    String title;
    String description;
    String locationName;
    boolean anonymous;
    boolean capsule;
    LocalDateTime unlockTime;
    int likeCount;
    int commentsCount;
    Integer unlockCount;
    UserResponse user;
    List<EchoMediaResponse> media;
    List<CommentResponse> comments;
    boolean isLike;
    LocalDateTime createdAt;
}