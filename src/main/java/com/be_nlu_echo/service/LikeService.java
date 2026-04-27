package com.be_nlu_echo.service;


import com.be_nlu_echo.dto.request.LikeRequest;
import com.be_nlu_echo.entity.Echo;
import com.be_nlu_echo.entity.EchoLike;
import com.be_nlu_echo.repository.LikeRepository;
import com.be_nlu_echo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class LikeService {
    UserRepository userRepository;
    EchoService echoService;
    LikeRepository likeRepository;

    public boolean likeEcho(Long echoId, LikeRequest request) {
        EchoLike like = new EchoLike();
        like.setUser(userRepository.findById(request.userId).orElseThrow(() -> new RuntimeException("User not found")));
        like.setEcho(echoService.getEchoById(echoId));
        likeRepository.save(like);
        return true;

    }
}
