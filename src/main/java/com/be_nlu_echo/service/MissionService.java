package com.be_nlu_echo.service;


import com.be_nlu_echo.dto.respone.MissionOverviewResponse;
import com.be_nlu_echo.dto.respone.MissionSummaryResponse;
import com.be_nlu_echo.dto.respone.SliceResponse;
import com.be_nlu_echo.dto.respone.UserMissionResponse;
import com.be_nlu_echo.entity.UserLevelSummary;
import com.be_nlu_echo.entity.UserMission;
import com.be_nlu_echo.enums.MissionCategory;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.enums.UserMissionStatus;
import com.be_nlu_echo.exception.AppException;

import com.be_nlu_echo.repository.UserLevelSummaryRepository;
import com.be_nlu_echo.repository.UserMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService  {

    private final UserLevelSummaryRepository userLevelSummaryRepository;
    private final UserMissionRepository userMissionRepository;

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

        Integer xpToNextLevel = 0;
        Double progressPercent = 1.0;

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

    public SliceResponse<UserMissionResponse> getMyMissionByCategory(Long userId, int page, int limit, MissionCategory category) {
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

    private UserMissionResponse mapToUserMissionResponse(UserMission um) {
        return UserMissionResponse.builder()
                .missionId(um.getMission().getId())
                .title(um.getMission().getTitle())
                .description(um.getMission().getDescription())
                .category(um.getMission().getCategory())
                .targetValue(um.getMission().getTargetValue())
                .rewardXp(um.getMission().getRewardXp())
                .rewardCoin(um.getMission().getRewardCoin())
                .status(um.getStatus())
                .progress(um.getProgress())
                .build();
    }
}