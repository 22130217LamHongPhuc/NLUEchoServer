package com.be_nlu_echo.dto.respone;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MediaUploadResponse {
    private String publicId;
    private String secureUrl;
    private String resourceType;
    private String format;
    private Long bytes;


}
