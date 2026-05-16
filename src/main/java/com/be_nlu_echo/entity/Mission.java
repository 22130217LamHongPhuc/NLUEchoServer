package com.be_nlu_echo.entity;

import com.be_nlu_echo.enums.*;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "mission",
        indexes = {
                @Index(name = "idx_mission_category_status", columnList = "category,status"),
                @Index(name = "idx_mission_event_type_status", columnList = "event_type,status"),
                @Index(name = "idx_mission_active", columnList = "is_active")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", length = 500)
    private String description;


    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private MissionType type;

    // Tab UI: DAILY, EXPLORATION, CREATE, CAPSULE, ACHIEVEMENT
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private MissionCategory category;


    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 50)
    private MissionEventType eventType;

    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "target_value", nullable = false)
    private Integer targetValue;

    @Builder.Default
    @Column(name = "reward_xp", nullable = false)
    private Integer rewardXp = 0;

    @Builder.Default
    @Column(name = "reward_coin", nullable = false)
    private Integer rewardCoin = 0;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MissionStatus status = MissionStatus.ACTIVE;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "mission")
    private List<UserMission> userMissions = new ArrayList<>();

    public boolean isAvailableNow() {
        LocalDateTime now = LocalDateTime.now();

        boolean afterStart = startAt == null || !now.isBefore(startAt);
        boolean beforeEnd = endAt == null || !now.isAfter(endAt);

        return Boolean.TRUE.equals(active)
                && status == MissionStatus.ACTIVE
                && afterStart
                && beforeEnd;
    }
}