package com.be_nlu_echo.dto.respone;

import com.be_nlu_echo.enums.EchoType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Builder
public class EchoHistoryItemResponse {
    Long echoId;
    String location;
    EchoType echoType;
    LocalDateTime unlockTime;
    String title;
    MediaResponse mediaResponse;
}
