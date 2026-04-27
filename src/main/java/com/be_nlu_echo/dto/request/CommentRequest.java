package com.be_nlu_echo.dto.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    Long echoId;
    Long userId;
    String content;
}
