package com.be_nlu_echo.repository;

import com.be_nlu_echo.dto.projection.EchoHistoryItemProjection;
import com.be_nlu_echo.entity.EchoUnlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EchoUnLockRepository extends JpaRepository<EchoUnlock, Long> {

    @Query("""
                SELECT COUNT(DISTINCT eu.echo.id)
                FROM EchoUnlock eu
                WHERE eu.user.id = :userId
            """)
    long countDistinctUnlockedEchoes(Long userId);


    boolean existsByEcho_IdAndUser_Id(Long echoId, Long userId);

    @Query(value = """
    SELECT
        e.id AS echoId,
        e.echo_type AS echoType,
        e.location_name AS location,
        eu.unlocked_at AS unlockTime,
        e.title AS title,
        em.media_url AS mediaUrl
    FROM echo_unlocks eu
    JOIN echoes e ON e.id = eu.echo_id
    LEFT JOIN echo_media em ON em.echo_id = e.id
       AND em.sort_order = 0
    WHERE eu.user_id = :userId
    ORDER BY eu.unlocked_at DESC
""", nativeQuery = true)
    List<EchoHistoryItemProjection> findEchoHistoryItems(
            @Param("userId") Long userId
    );

}
