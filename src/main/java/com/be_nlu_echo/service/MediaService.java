package com.be_nlu_echo.service;

import com.be_nlu_echo.dto.respone.MediaUploadResponse;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.exception.AppException;
import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MediaService {

    private final Cloudinary cloudinary;

    public MediaService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public MediaUploadResponse upload(MultipartFile file, String folder) throws IOException {
        validateFile(file);

        Map<String, Object> options = new HashMap<>();
        options.put("folder", folder);
        options.put("resource_type", "auto");

        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), options);

        String publicId = (String) result.get("public_id");
        String secureUrl = (String) result.get("secure_url");
        String resourceType = (String) result.get("resource_type");
        String format = (String) result.get("format");
        Long bytes = ((Number) result.get("bytes")).longValue();

        return new MediaUploadResponse(publicId, secureUrl, resourceType, format, bytes);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException("File is empty", StatusCode.INVALID_FILE);
        }

        String contentType = file.getContentType();
        long maxImageSize = 2 * 1024 * 1024;
        long maxAudioSize = 10 * 1024 * 1024;

        if (contentType == null) {
            throw new AppException("Missing content type",StatusCode.INVALID_FILE);
        }

        boolean isImage = contentType.startsWith("image/");
        boolean isAudio = contentType.startsWith("audio/");

        if (!isImage && !isAudio) {
            throw new AppException("Only image or audio files are allowed",StatusCode.INVALID_FILE);
        }

        if (isImage && file.getSize() > maxImageSize) {
            throw new AppException("Image file too large",StatusCode.INVALID_FILE);
        }

        if (isAudio && file.getSize() > maxAudioSize) {
            throw new AppException("Audio file too large",StatusCode.INVALID_FILE);
        }
    }
}
