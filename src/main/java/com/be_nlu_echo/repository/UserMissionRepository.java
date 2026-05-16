package com.be_nlu_echo.repository;

import com.be_nlu_echo.dto.projection.MissionMeProjection;
import com.be_nlu_echo.dto.respone.SliceResponse;
import com.be_nlu_echo.dto.respone.UserMissionResponse;
import com.be_nlu_echo.entity.Mission;
import com.be_nlu_echo.entity.UserMission;
import com.be_nlu_echo.enums.MissionCategory;
import com.be_nlu_echo.enums.MissionEventType;
import com.be_nlu_echo.enums.MissionStatus;
import com.be_nlu_echo.enums.UserMissionStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {

    List<UserMission> findByUserId(Long userId);

    List<UserMission> findByUserIdAndMission_Category(Long userId, MissionCategory category);

    Optional<UserMission> findByUserIdAndMission_Id(Long userId, Long missionId);

    long countByUser_IdAndStatus(Long userId, UserMissionStatus status);





    @Query(
            value = """
                SELECT
                    m.id AS missionId,
                    um.id AS userMissionId,

                    m.title AS title,
                    m.description AS description,
                    m.category AS category,
                    m.type AS type,
                    m.event_type AS eventType,
                    m.icon AS icon,

                    m.target_value AS targetValue,
                    m.reward_xp AS rewardXp,
                    m.reward_coin AS rewardCoin,

                    COALESCE(um.progress, 0) AS progress,
                    COALESCE(um.status, 'IN_PROGRESS') AS status,

                    um.completed_at AS completedAt,
                    um.claimed_at AS claimedAt
                FROM mission m
                LEFT JOIN user_mission um
                    ON um.mission_id = m.id
                   AND um.user_id = :userId
                WHERE m.is_active = 1
                  AND m.status = 'ACTIVE'
                  AND m.category = :category
                ORDER BY
                    CASE
                        WHEN um.status = 'COMPLETED' THEN 1
                        WHEN um.status = 'IN_PROGRESS' AND um.progress > 0 THEN 2
                        WHEN um.id IS NULL THEN 3
                        WHEN um.status = 'IN_PROGRESS' AND um.progress = 0 THEN 3
                        WHEN um.status = 'CLAIMED' THEN 4
                        ELSE 5
                    END ASC,
                    COALESCE(um.progress / NULLIF(m.target_value, 0), 0) DESC,
                    m.reward_xp DESC,
                    m.created_at DESC
                """,
            nativeQuery = true
    )
    Slice<MissionMeProjection> findMyMissionsByCategory(
            @Param("userId") Long userId,
            @Param("category") String category,
            Pageable pageable
    );


    Optional<UserMission> findByUser_IdAndMission_Id(Long userId, Long id);
}



