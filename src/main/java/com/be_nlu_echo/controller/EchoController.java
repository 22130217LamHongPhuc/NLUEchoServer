package com.be_nlu_echo.controller;


import com.be_nlu_echo.dto.request.RequestCreateEcho;
import com.be_nlu_echo.dto.respone.ApiResponse;
import com.be_nlu_echo.dto.respone.CreateEchoResponse;
import com.be_nlu_echo.dto.respone.EchoListItemResponse;
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
       CreateEchoResponse response =  echoService.createEcho(request, User.builder().id(5L).build());

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                StatusCode.SUCCESS,
                "Echo created successfully",
                response
        ));
    }


}