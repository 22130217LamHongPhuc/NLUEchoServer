package com.be_nlu_echo.controller;


import com.be_nlu_echo.dto.request.RequestCreateEcho;
import com.be_nlu_echo.dto.respone.*;
import com.be_nlu_echo.entity.User;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.service.EchoService;
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
        List<EchoListItemResponse> response = echoService.getAllEchos();

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                StatusCode.SUCCESS,
                "Echo list retrieved successfully",
                response
        ));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateEchoResponse>> createEcho(
            @RequestBody RequestCreateEcho request
//            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        System.out.println("Request received: " + request);
        CreateEchoResponse response = echoService.createEcho(request, User.builder().id(5L).build());

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                StatusCode.SUCCESS,
                "Echo created successfully",
                response
        ));
    }

    @GetMapping("/echo-previews")
    public ResponseEntity<ApiResponse<List<EchoPreviewResponse>>> getEchoPreviews(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<EchoPreviewResponse> response = echoService.getEchoPreviews(latitude, longitude, page, size);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        StatusCode.SUCCESS,
                        " echoPreviews retrieved successfully",
                        response
                )
        );

    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<EchoDetailResponse>> getEchoDetail(@PathVariable Long id) {
        EchoDetailResponse response = echoService.getEchoDetail(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        StatusCode.SUCCESS,
                        "Echo detail retrieved successfully",
                        response
                )
        );
    }


}