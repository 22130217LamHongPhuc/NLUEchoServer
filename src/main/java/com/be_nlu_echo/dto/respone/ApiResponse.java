package com.be_nlu_echo.dto.respone;

import com.be_nlu_echo.enums.StatusCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private StatusCode code;



    public ApiResponse(boolean success, StatusCode code,String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
    }

}
