package com.be_nlu_echo.controller;

import com.be_nlu_echo.dto.request.CommentRequest;
import com.be_nlu_echo.dto.request.LikeRequest;
import com.be_nlu_echo.dto.respone.ApiResponse;
import com.be_nlu_echo.dto.respone.CommentResponse;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.service.CommentService;
import com.be_nlu_echo.service.EchoService;
import com.be_nlu_echo.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/echo/{echoId}/comments")
@AllArgsConstructor
public class EchoCommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable Long echoId
    ) {
        List<CommentResponse> comments = commentService.getCommentsByEchoId(echoId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        StatusCode.SUCCESS,
                        "Comments retrieved successfully",
                        comments
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long echoId,
            @RequestBody CommentRequest request
    ) {
        CommentResponse comment = commentService.createComment(echoId, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        StatusCode.SUCCESS,
                        "Comment created successfully",
                        comment
                )
        );
    }
}
