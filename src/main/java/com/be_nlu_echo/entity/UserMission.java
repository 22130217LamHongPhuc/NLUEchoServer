package com.be_nlu_echo.entity;

import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.enums.UserMissionStatus;
import com.be_nlu_echo.exception.AppException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_mission",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_mission_user_id_mission_id",
                        columnNames = {"user_id", "mission_id"}
                )
        },
        indexes = {
                @Index(name = "idx_user_mission_user_status", columnList = "user_id,status"),
                @Index(name = "idx_user_mission_mission", columnList = "mission_id"),
                @Index(name = "idx_user_mission_updated_at", columnList = "updated_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Builder.Default
    @Column(name = "progress", nullable = false)
    private Integer progress = 0;


    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private UserMissionStatus status = UserMissionStatus.IN_PROGRESS;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "claimed_at")
    private LocalDateTime claimedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void increaseProgress(int amount) {
        if (amount <= 0 || mission == null) {
            return;
        }

        if (status == UserMissionStatus.COMPLETED ||
                status == UserMissionStatus.CLAIMED) {
            return;
        }

        int currentProgress = progress == null ? 0 : progress;
        int targetValue = mission.getTargetValue() == null ? 0 : mission.getTargetValue();

        int newProgress = currentProgress + amount;
        this.progress = Math.min(newProgress, targetValue);

        if (this.progress >= targetValue) {
            this.status = UserMissionStatus.COMPLETED;
            this.completedAt = LocalDateTime.now();
        } else {
            this.status = UserMissionStatus.IN_PROGRESS;
        }
    }

    public void claim() {
        if (status != UserMissionStatus.COMPLETED) {
            throw new AppException("Mission is not completed yet", StatusCode.INVALID_STATUS);
        }

        this.status = UserMissionStatus.CLAIMED;
        this.claimedAt = LocalDateTime.now();
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public Long getMissionId() {
        return mission != null ? mission.getId() : null;
    }
}