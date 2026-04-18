package com.be_nlu_echo.dto.respone;


import com.be_nlu_echo.enums.EchoType;
import com.be_nlu_echo.enums.Visibility;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CreateEchoResponse {

    private Long id;
    private String title;
    private String content;
    private EchoType echoType;

    private double latitude;
    private double longitude;
    private Double gpsAccuracy;
    private String locationName;

    private Integer unlockRadiusM;

    private boolean anonymous;
    private boolean capsule;
    private LocalDateTime unlockAt;

    private Visibility visibility;
    private String status;

    private Integer likeCount;
    private Integer commentCount;
    private Integer unlockCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<MediaResponse> mediaList;
}