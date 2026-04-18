package com.be_nlu_echo.repository;

import com.be_nlu_echo.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<List<RefreshToken>> findAllByUserId(Long userId);

}
