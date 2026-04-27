package com.be_nlu_echo.controller;

import com.be_nlu_echo.dto.request.LikeRequest;
import com.be_nlu_echo.dto.respone.ApiResponse;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.service.EchoService;
import com.be_nlu_echo.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/echo/{echoId}/likes")
@AllArgsConstructor
public class EchoLikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> likeEcho(
            @PathVariable Long echoId,
            @RequestBody LikeRequest request
    ) {
            boolean result = likeService.likeEcho(echoId, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        StatusCode.SUCCESS,
                        "Echo liked successfully",
                        result
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Boolean>> unlikeEcho(
            @PathVariable Long echoId,
            @RequestBody LikeRequest request
    ) {
//        boolean result = likeService.unlikeEcho(echoId, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        StatusCode.SUCCESS,
                        "Echo unliked successfully",
                        true
                )
        );
    }
}