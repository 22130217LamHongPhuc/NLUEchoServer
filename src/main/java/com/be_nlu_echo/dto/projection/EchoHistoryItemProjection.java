package com.be_nlu_echo.dto.projection;

import com.be_nlu_echo.enums.EchoType;

import java.time.LocalDateTime;

public interface EchoHistoryItemProjection {
    Long getEchoId();
    EchoType getEchoType();
    String getLocation();
    LocalDateTime getUnlockTime();
    String getTitle();
    String getMediaUrl();
}