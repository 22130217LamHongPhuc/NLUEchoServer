package com.be_nlu_echo.repository;

import com.be_nlu_echo.entity.EchoLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<EchoLike, Long> {
    boolean existsByEchoIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT COUNT(DISTINCT el.echo.id)
            FROM EchoLike el
            WHERE el.user.id = :userId
        """)
    long countLikedEchoesByUserId(Long userId);
}
