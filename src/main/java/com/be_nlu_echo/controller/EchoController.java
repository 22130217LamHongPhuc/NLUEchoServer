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
import com.be_nlu_echo.service.EchoService;
import com.be_nlu_echo.service.MissionService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/echo")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class EchoController {

     EchoService echoService;



    @GetMapping
    public ResponseEntity<ApiResponse<List<EchoListItemResponse>>> getAllEchos() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        StatusCode.SUCCESS,
                        "Echo list retrieved successfully",
                        echoService.getAllEchos()
                )
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateEchoResponse>> createEcho(
            @RequestBody RequestCreateEcho request
    ) {

        CreateEchoResponse response = echoService.createEcho(request, User.builder().id(5L).build());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        StatusCode.SUCCESS,
                        "Echo created successfully",
                        response
                )
        );
    }

    @GetMapping("/echo-previews")
    public ResponseEntity<ApiResponse<List<EchoPreviewResponse>>> getEchoPreviews(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        StatusCode.SUCCESS,
                        "Echo previews retrieved successfully",
                        echoService.getEchoPreviews(latitude, longitude, page, size)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EchoDetailResponse>> getEchoDetail(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        StatusCode.SUCCESS,
                        "Echo detail retrieved successfully",
                        echoService.getEchoDetail(id, 5L)
                )
        );
    }
}