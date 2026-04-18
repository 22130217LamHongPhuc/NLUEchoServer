package com.be_nlu_echo.dto.request;

import jakarta.validation.Valid;
import lombok.Getter;

@Getter
public class RegisterRequest {
    private String fullname;
    private String email;
    private String password;

     public RegisterRequest(String fullname, String email, String password) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
    }
}
