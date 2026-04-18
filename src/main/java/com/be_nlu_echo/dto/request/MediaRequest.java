package com.be_nlu_echo.dto.request;

import lombok.Data;

@Data
public class MediaRequest {
    private String url;
    private String publicId;
    private String mimeType;
    private Integer duration;
}