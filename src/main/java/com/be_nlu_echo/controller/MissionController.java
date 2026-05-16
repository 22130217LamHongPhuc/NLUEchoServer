package com.be_nlu_echo.controller;


import com.be_nlu_echo.dto.respone.ApiResponse;
import com.be_nlu_echo.dto.respone.MissionOverviewResponse;
import com.be_nlu_echo.dto.respone.SliceResponse;
import com.be_nlu_echo.dto.respone.UserMissionResponse;
import com.be_nlu_echo.enums.MissionCategory;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.service.CustomUserDetails;
import com.be_nlu_echo.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missions")

public class MissionController {
    @Autowired
    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping("/me/overview")
    public ResponseEntity<ApiResponse<MissionOverviewResponse>> getMyMissionOverview(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        MissionOverviewResponse response = missionService.getMyMissionOverview(userDetails.getUserId());

      return ResponseEntity.ok(
              new ApiResponse<>(
                      true,
                      StatusCode.SUCCESS,
                      "Mission overview retrieved successfully",
                      response
              )
      );
    }

    @GetMapping("/me/category")
    public ResponseEntity<ApiResponse<SliceResponse<UserMissionResponse>>> getMyMissionByCategory(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "DAILY") String category,
            @AuthenticationPrincipal CustomUserDetails userDetails

            ) {

        MissionCategory missionCategory = MissionCategory.valueOf(category.toUpperCase());
        SliceResponse<UserMissionResponse> response = missionService.getMyMissionByCategory(userDetails.getUserId(),page,limit,missionCategory);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        StatusCode.SUCCESS,
                        "Missions by category retrieved successfully",
                        response
                )
        );
    }


}