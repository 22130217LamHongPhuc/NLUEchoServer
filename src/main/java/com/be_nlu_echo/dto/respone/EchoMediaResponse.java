package com.be_nlu_echo.dto.respone;

import com.be_nlu_echo.enums.MediaType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EchoMediaResponse {

    private Long id;

    private MediaType mediaType;
    private String mediaUrl;

    private String mimeType;
    private Long fileSizeBytes;

    private Integer durationSeconds;

    private Integer width;
    private Integer height;

    private Integer sortOrder;
}