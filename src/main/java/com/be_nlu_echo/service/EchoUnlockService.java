package com.be_nlu_echo.service;


import com.be_nlu_echo.dto.respone.EchoHistoryItemResponse;
import com.be_nlu_echo.dto.respone.MediaResponse;
import com.be_nlu_echo.entity.Echo;
import com.be_nlu_echo.entity.EchoUnlock;
import com.be_nlu_echo.entity.User;
import com.be_nlu_echo.repository.EchoUnLockRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true,level = lombok.AccessLevel.PRIVATE)
public class EchoUnlockService {
    private final EchoUnLockRepository echoUnLockRepository;

    public boolean unlockEcho(Long echoId, Long userId) {

        boolean existed = echoUnLockRepository.existsByEcho_IdAndUser_Id(
                echoId,
                userId
        );

        if (existed) {
            return false;
        }

        EchoUnlock unlock = EchoUnlock.builder()
                .echo(Echo.builder().id(echoId).build())
                .user(User.builder().id(userId).build())
                .unlockedAt(LocalDateTime.now())
                .build();

        echoUnLockRepository.save(unlock);

        return true;
    }

        public List<EchoHistoryItemResponse> getEchoHistory(Long userId) {
            return echoUnLockRepository.findEchoHistoryItems(userId)
                    .stream()
                    .map(eu -> EchoHistoryItemResponse.builder()
                            .echoId(eu.getEchoId())
                            .location(eu.getLocation())
                            .title(eu.getTitle())
                            .echoType(eu.getEchoType())
                            .unlockTime(eu.getUnlockTime())
                            .mediaResponse(MediaResponse.builder().url(eu.getMediaUrl())
                            .build()).build()).toList();
        }

}
