package com.be_nlu_echo.controller;


import com.be_nlu_echo.dto.request.CommentRequest;
import com.be_nlu_echo.dto.request.LikeRequest;
import com.be_nlu_echo.dto.request.RequestCreateEcho;
import com.be_nlu_echo.dto.respone.*;
import com.be_nlu_echo.entity.EchoLike;
import com.be_nlu_echo.entity.User;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.repository.LikeRepository;
import com.be_nlu_echo.repository.UserRepository;
import com.be_nlu_echo.service.CustomUserDetails;
import com.be_nlu_echo.service.EchoService;
import com.be_nlu_echo.service.EchoUnlockService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/me/echo-history")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class EchoHistoryController {

    private final EchoService echoService;
    private final EchoUnlockService echoUnlockService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<EchoHistorySummaryResponse>> getEchoHistorySummary(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        StatusCode.SUCCESS,
                        "Echo history summary retrieved successfully",
                        echoService.getEchoHistorySummary(userDetails.getUserId())
                )
        );
    }

    @GetMapping("/view")
    public ResponseEntity<ApiResponse<List<EchoHistoryItemResponse>>> getEchoHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        StatusCode.SUCCESS,
                        "Echo history retrieved successfully",
                        echoUnlockService.getEchoHistory(userDetails.getUserId())
                )
        );
    }
}