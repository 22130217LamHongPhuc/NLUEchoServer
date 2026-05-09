package com.be_nlu_echo.controller;



import com.be_nlu_echo.dto.request.CommentRequest;
import com.be_nlu_echo.dto.request.LikeRequest;
import com.be_nlu_echo.dto.request.RequestCreateEcho;
import com.be_nlu_echo.dto.respone.*;
import com.be_nlu_echo.entity.Echo;
import com.be_nlu_echo.entity.EchoLike;
import com.be_nlu_echo.entity.EchoUnlock;
import com.be_nlu_echo.entity.User;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.repository.EchoUnLockRepository;
import com.be_nlu_echo.repository.LikeRepository;
import com.be_nlu_echo.repository.UserRepository;
import com.be_nlu_echo.service.CustomUserDetails;
import com.be_nlu_echo.service.EchoService;
import com.be_nlu_echo.service.EchoUnlockService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/echo")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class EchoUnlockController {

    private final EchoUnlockService echoUnlockService;

        @PostMapping("/{echoId}/unlock")
        public ResponseEntity<ApiResponse<Boolean>> unLockEcho(
                @PathVariable Long echoId,
                @AuthenticationPrincipal CustomUserDetails userDetails
        ) {
            boolean unlocked = echoUnlockService.unlockEcho(echoId, userDetails.getUserId());

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            StatusCode.SUCCESS,
                            unlocked ? "Echo unlocked successfully" : "Echo already unlocked",
                            null
                    )
            );
        }

}
