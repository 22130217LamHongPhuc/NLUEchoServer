package com.be_nlu_echo.dto.request;

import com.be_nlu_echo.enums.EchoType;
import com.be_nlu_echo.enums.Visibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RequestCreateEcho {
    String title;
    String content;
    EchoType echoType;
    double latitude;
    double longitude;
    double gpsAccuracy;
    String locationName;
    boolean anonymous;
    boolean capsule;
    LocalDateTime unlockAt;
    List<MediaRequest> images;
    MediaRequest audio;
    Visibility visibility;

}
