package com.be_nlu_echo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "v_user_level_summary")
@Immutable
@Getter
public class UserLevelSummary {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "total_xp")
    private Integer totalXp;

    @Column(name = "coins")
    private Integer coins;

    @Column(name = "streak_days")
    private Integer streakDays;

    @Column(name = "level_no")
    private Integer levelNo;

    @Column(name = "level_title")
    private String levelTitle;

    @Column(name = "current_level_required_xp")
    private Integer currentLevelRequiredXp;

    @Column(name = "next_level_required_xp")
    private Integer nextLevelRequiredXp;

    @Column(name = "current_xp")
    private Integer currentXp;

    @Column(name = "next_level_xp")
    private Integer nextLevelXp;
}