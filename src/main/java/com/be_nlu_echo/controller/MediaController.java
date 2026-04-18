package com.be_nlu_echo.controller;

import com.be_nlu_echo.dto.respone.ApiResponse;
import com.be_nlu_echo.dto.respone.MediaUploadResponse;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<MediaUploadResponse>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", defaultValue = "general") String type
    ) throws IOException {

        String folder = switch (type) {
            case "avatar" -> "nlu_echo/avatars";
            case "post" -> "nlu_echo/posts";
            case "audio" -> "nlu_echo/audios";
            default -> "nlu_echo/general";
        };

        MediaUploadResponse response = mediaService.upload(file, folder);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                StatusCode.SUCCESS,
                "File uploaded successfully",
                response
        ));
    }
}