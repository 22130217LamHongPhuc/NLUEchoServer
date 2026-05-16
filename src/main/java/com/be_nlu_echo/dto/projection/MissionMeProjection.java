package com.be_nlu_echo.dto.projection;

import com.be_nlu_echo.enums.MissionCategory;
import com.be_nlu_echo.enums.UserMissionStatus;

import java.time.LocalDateTime;

public interface MissionMeProjection {
    Long getMissionId();
    Long getUserMissionId();

    String getTitle();
    String getDescription();

    MissionCategory getCategory();
    String getType();
    String getEventType();
    String getIcon();

    Integer getTargetValue();
    Integer getRewardXp();
    Integer getRewardCoin();

    Integer getProgress();
    UserMissionStatus getStatus();

    LocalDateTime getCompletedAt();
    LocalDateTime getClaimedAt();
}