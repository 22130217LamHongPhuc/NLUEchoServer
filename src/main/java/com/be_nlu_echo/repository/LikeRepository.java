package com.be_nlu_echo.repository;

import com.be_nlu_echo.entity.EchoLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<EchoLike, Long> {
    boolean existsByEchoIdAndUserId(Long id, Long userId);
}
