package com.be_nlu_echo.dto.projection;

import com.be_nlu_echo.enums.MissionCategory;
import com.be_nlu_echo.enums.MissionStatus;

import java.time.LocalDateTime;

public interface MissionMeProjection {
    Long getMissionId();
    Long getUserMissionId();

    String getTitle();
    String getDescription();
    MissionCategory getCategory();
    String getType();
    String getIcon();

    Integer getTargetValue();
    Integer getRewardXp();
    Integer getRewardCoin();

    Integer getProgress();
    MissionStatus getStatus();

    LocalDateTime getCompletedAt();
    LocalDateTime getClaimedAt();
}