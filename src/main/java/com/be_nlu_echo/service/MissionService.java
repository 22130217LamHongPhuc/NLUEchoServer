package com.be_nlu_echo.service;


import com.be_nlu_echo.dto.projection.MissionMeProjection;
import com.be_nlu_echo.dto.respone.MissionOverviewResponse;
import com.be_nlu_echo.dto.respone.MissionSummaryResponse;
import com.be_nlu_echo.dto.respone.SliceResponse;
import com.be_nlu_echo.dto.respone.UserMissionResponse;
import com.be_nlu_echo.entity.*;
import com.be_nlu_echo.enums.*;
import com.be_nlu_echo.exception.AppException;

import com.be_nlu_echo.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService  {

    private final UserLevelSummaryRepository userLevelSummaryRepository;
    private final UserMissionRepository userMissionRepository;
    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final EchoRepository echoRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final EchoUnLockRepository echoUnLockRepository;

    public MissionOverviewResponse getMyMissionOverview(Long userId) {
        UserLevelSummary levelSummary = userLevelSummaryRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AppException("User not found",StatusCode.USER_NOT_FOUND));

        long inProgressCount = userMissionRepository.countByUser_IdAndStatus(
                userId,
                UserMissionStatus.IN_PROGRESS
        );

        long claimableCount = userMissionRepository.countByUser_IdAndStatus(
                userId,
                UserMissionStatus.COMPLETED
        );

        long claimedCount = userMissionRepository.countByUser_IdAndStatus(
                userId,
                UserMissionStatus.CLAIMED
        );

        long completedCount = claimableCount + claimedCount;

        Integer totalXp = safe(levelSummary.getTotalXp());
        Integer currentXp = safe(levelSummary.getCurrentXp());
        Integer nextLevelXp = safe(levelSummary.getNextLevelXp());
        Integer nextLevelRequiredTotalXp = levelSummary.getNextLevelRequiredXp();

        int xpToNextLevel = 0;
        double progressPercent = 1.0;

        if (nextLevelRequiredTotalXp != null && nextLevelXp > 0) {
            xpToNextLevel = Math.max(nextLevelRequiredTotalXp - totalXp, 0);
            progressPercent = Math.min(currentXp.doubleValue() / nextLevelXp.doubleValue(), 1.0);
        }

        MissionSummaryResponse missionSummary = MissionSummaryResponse.builder()
                .inProgress(inProgressCount)
                .claimable(claimableCount)
                .completed(completedCount)
                .build();

        return MissionOverviewResponse.builder()
                .level(levelSummary.getLevelNo())
                .levelTitle(levelSummary.getLevelTitle())
                .totalXp(totalXp)
                .currentLevelXp(currentXp)
                .nextLevelXp(nextLevelXp)
                .nextLevelRequiredTotalXp(nextLevelRequiredTotalXp)
                .xpToNextLevel(xpToNextLevel)
                .progressPercent(progressPercent)
                .coins(safe(levelSummary.getCoins()))
                .streakDays(safe(levelSummary.getStreakDays()))
                .missionSummary(missionSummary)
                .build();
    }

    private Integer safe(Integer value) {
        return value == null ? 0 : value;
    }

        public SliceResponse<UserMissionResponse> getMyMissionByCategory(Long userId, int page, int limit, String category) {

            System.out.println("Fetching missions for userId: " + userId + ", category: " + category + ", page: " + page + ", limit: " + limit);
            List<UserMissionResponse> list = userMissionRepository.findMyMissionsByCategory(
                    userId, category, PageRequest.of(page, limit)
            ).getContent().stream()
                    .map(this::mapToUserMissionResponse).toList();

            return SliceResponse.<UserMissionResponse>builder().
                    content(list)
                    .size(list.size())
                    .page(page)
                    .build();
        }

    private UserMissionResponse mapToUserMissionResponse(MissionMeProjection um) {
        int progress = um.getProgress() != null ? um.getProgress() : 0;
        int targetValue = um.getTargetValue() != null && um.getTargetValue() > 0
                ? um.getTargetValue()
                : 1;

        Integer progressPercent = Math.min(
                100,
                (int) Math.round((progress * 100.0) / targetValue)
        );

        UserMissionStatus status = um.getStatus() != null
                ? um.getStatus()
                : UserMissionStatus.IN_PROGRESS;

        Boolean claimable = status == UserMissionStatus.COMPLETED;

        return UserMissionResponse.builder()
                .missionId(um.getMissionId())
                .userMissionId(um.getUserMissionId())
                .title(um.getTitle())
                .description(um.getDescription())
                .category(um.getCategory())
                .type(um.getType())
                .eventType(um.getEventType())
                .icon(um.getIcon())
                .progress(progress)
                .targetValue(targetValue)
                .progressPercent(progressPercent)
                .rewardXp(um.getRewardXp())
                .rewardCoin(um.getRewardCoin())
                .status(status)
                .claimable(claimable)
                .completedAt(um.getCompletedAt())
                .claimedAt(um.getClaimedAt())
                .build();
    }



    @Transactional
    public void handleEvent(MissionEventType eventType,User user){
        List<Mission> missions = missionRepository.findByEventTypeAndStatus(eventType, MissionStatus.ACTIVE);

        for (Mission mission: missions){
            UserMission userMission = userMissionRepository.findByUser_IdAndMission_Id(user.getId(), mission.getId())
                    .orElseGet(() -> {
                        UserMission newUserMission = UserMission.builder()
                                .user(user)
                                .mission(mission)
                                .progress(0)
                                .status(UserMissionStatus.IN_PROGRESS)
                                .build();
                        return userMissionRepository.save(newUserMission);
                    });



            if (userMission.getStatus() == UserMissionStatus.CLAIMED) {
                continue;
            }

            if (userMission.getStatus() == UserMissionStatus.COMPLETED) {
                continue;
            }


            long newProgress = resolveProgress(user, mission, eventType, userMission);

            long targetValue = mission.getTargetValue() == null || mission.getTargetValue() <= 0
                    ? 1
                    : mission.getTargetValue();

            userMission.setProgress((int) Math.min(newProgress, targetValue));

            if (userMission.getProgress() >= targetValue) {
                userMission.setStatus(UserMissionStatus.COMPLETED);
                userMission.setCompletedAt(LocalDateTime.now());
            }

            userMissionRepository.save(userMission);


        }
    }

    private long resolveProgress(User user, Mission mission, MissionEventType eventType, UserMission userMission) {
        switch(mission.getProgressStrategy()){
            case INCREMENT -> {
                return userMission.getProgress() + 1;
            }
            case COUNT_TOTAL, COUNT_DISTINCT_LOCATION -> {
                return resolveCountTotalProgress(user, eventType);
            }
        }

        return -1;
    }

    private long resolveCountTotalProgress(
            User user,
            MissionEventType eventType
    ) {
        return switch (eventType) {
            case ECHO_CREATED -> echoRepository.countByUser_IdAndCapsule(user.getId(),false);

            case ECHO_UNLOCKED -> echoUnLockRepository.countDistinctUnlockedEchoes(user.getId());

            case ECHO_LIKED -> likeRepository.countLikedEchoesByUserId(user.getId());

            case ECHO_COMMENTED -> commentRepository.countByUser_Id(user.getId());

            case USER_FOLLOWED -> 1;

            case CAPSULE_CREATED -> echoRepository.countByUser_IdAndCapsule(user.getId(),true);


        };
    }

    public boolean claimMissionReward(User user, Long missionId) {
        UserMission userMission = userMissionRepository.findByUser_IdAndMission_Id(user.getId(), missionId)
                .orElseThrow(() -> new AppException("Mission not found for user", StatusCode.NOT_FOUND));

        userMission.claim();

        missionRepository.save(userMission.getMission());
        user.setCoins(user.getCoins() + userMission.getMission().getRewardCoin());
        user.setTotalXp(user.getTotalXp() + userMission.getMission().getRewardXp());
        userRepository.save(user);

        return true;
    }
}

