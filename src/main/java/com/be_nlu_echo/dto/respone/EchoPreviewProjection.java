package com.be_nlu_echo.dto.respone;

import java.time.LocalDateTime;



public interface EchoPreviewProjection {

    Long getId();
    String getTitle();
    String getEchoType();
    String getLocationName();
    Boolean getAnonymous();
    Boolean getCapsule();
    LocalDateTime getUnlockTime();
    String getVisibility();
    Integer getLikeCount();
    Integer getCommentCount();
    Integer getUnlockCount();
    LocalDateTime getCreatedAt();
    Double getLongitude();
    Double getLatitude();
    Double getDistance();
    Integer getUnlockRadiusM();
}
