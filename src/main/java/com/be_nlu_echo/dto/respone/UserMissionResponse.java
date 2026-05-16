package com.be_nlu_echo.dto.respone;

import com.be_nlu_echo.enums.MissionCategory;
import com.be_nlu_echo.enums.UserMissionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMissionResponse {

    private Long missionId;
    private Long userMissionId;

    private String title;
    private String description;

    private MissionCategory category;
    private String type;
    private String eventType;
    private String icon;

    private Integer progress;
    private Integer targetValue;

    private Integer rewardXp;
    private Integer rewardCoin;

    private UserMissionStatus status;
    private Boolean claimable;

    private LocalDateTime completedAt;
    private LocalDateTime claimedAt;
}