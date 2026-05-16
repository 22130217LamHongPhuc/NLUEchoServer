package com.be_nlu_echo.dto.respone;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionOverviewResponse {

    private Integer level;
    private String levelTitle;

    private Integer totalXp;
    private Integer currentLevelXp;
    private Integer nextLevelXp;
    private Integer nextLevelRequiredTotalXp;
    private Integer xpToNextLevel;
    private Double progressPercent;

    private Integer coins;
    private Integer streakDays;

    private MissionSummaryResponse missionSummary;
}