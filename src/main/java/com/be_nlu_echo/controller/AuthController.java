package com.be_nlu_echo.controller;

import com.be_nlu_echo.dto.request.AuthRequest;
import com.be_nlu_echo.dto.request.RefreshTokenRequest;
import com.be_nlu_echo.dto.request.RegisterRequest;
import com.be_nlu_echo.dto.respone.ApiResponse;
import com.be_nlu_echo.dto.respone.AuthResponse;
import com.be_nlu_echo.entity.RefreshToken;
import com.be_nlu_echo.entity.User;
import com.be_nlu_echo.enums.AuthProvider;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.enums.UserStatus;
import com.be_nlu_echo.exception.AppException;
import com.be_nlu_echo.repository.UserRepository;
import com.be_nlu_echo.service.CustomUserDetails;
import com.be_nlu_echo.service.JwtService;
import com.be_nlu_echo.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    public String test() {
        return "Hello";
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException("Email already in use", StatusCode.EMAIL_ALREADY_IN_USE);
        }

        User user = new User();
        user.setFullName(request.getFullname());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        user.setDefaultGhostMode(false);


        userRepository.save(user);

        String token = jwtService.generateToken(new CustomUserDetails(user));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                StatusCode.SUCCESS,
                "Registration successful",
                new AuthResponse(token, refreshToken.getToken())
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException("User not found with email: " + request.getEmail(), StatusCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                throw new AppException("Password is incorrect", StatusCode.PASSWORD_INCORRECT);
            }
        System.out.println("start auth : "+user.getEmail());

        String token = jwtService.generateToken(new CustomUserDetails(user));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                StatusCode.SUCCESS,
                "Login successful",
                new AuthResponse(token, refreshToken.getToken())
        ));
    }

    @PostMapping("/refresh")
        public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        System.out.println("refresh token "+request.getRefreshToken());
        RefreshToken rt = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        User user = rt.getUser();
        String token = jwtService.generateToken(new CustomUserDetails(user));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                StatusCode.SUCCESS,
                "Refresh token successful",
                new AuthResponse(token, rt.getToken())
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody RefreshTokenRequest tokenRequest) {
        refreshTokenService.revokeRefreshToken(tokenRequest.getRefreshToken());
        return  ResponseEntity.ok(new ApiResponse<>(true, StatusCode.SUCCESS, "Logout successful", null));
    }



}