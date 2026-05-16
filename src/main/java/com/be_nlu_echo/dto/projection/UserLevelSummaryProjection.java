package com.be_nlu_echo.dto.projection;


public interface UserLevelSummaryProjection {

    Long getUserId();

    String getFullName();

    Integer getTotalXp();

    Integer getCoins();

    Integer getStreakDays();

    Integer getLevelNo();

    String getLevelTitle();

    Integer getCurrentLevelRequiredXp();

    Integer getNextLevelRequiredXp();

    Integer getCurrentXp();

    Integer getNextLevelXp();
}