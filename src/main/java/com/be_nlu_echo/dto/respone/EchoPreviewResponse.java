package com.be_nlu_echo.dto.respone;

import com.be_nlu_echo.enums.EchoMode;
import com.be_nlu_echo.enums.EchoType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EchoPreviewResponse {
    private Long id;
    private String title;
    private EchoType echoType;
    private Double distance;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean anonymous;
    private Boolean capsule;
    private LocalDateTime unlockTime;
    private Double longitude;
    private Double latitude;
    private Integer unlockRadiusM;
    private EchoMode mode;



}