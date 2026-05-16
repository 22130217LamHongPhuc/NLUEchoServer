package com.be_nlu_echo.repository;

import com.be_nlu_echo.dto.respone.EchoPreviewProjection;
import com.be_nlu_echo.entity.Echo;
import com.be_nlu_echo.enums.EchoStatus;
import com.be_nlu_echo.enums.Visibility;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EchoRepository extends JpaRepository<Echo, Long> {
	@Query("""
			select e
			from Echo e
			join fetch e.user
			where e.status = :status and e.visibility = :visibility
			order by e.createdAt desc
			""")
	List<Echo> findFeedEchos(EchoStatus status, Visibility visibility);

	@Query(value = """
    SELECT
        e.id AS id,
        e.title AS title,
        e.echo_type AS echoType,
        e.location_name AS locationName,
        e.is_anonymous AS anonymous,
        e.is_capsule AS capsule,
        e.unlock_at AS unlockTime,
        e.visibility AS visibility,
        e.like_count AS likeCount,
        e.comment_count AS commentCount,
        e.unlock_count AS unlockCount,
        e.created_at AS createdAt,
        e.longitude AS longitude,
        e.latitude AS latitude,
        e.unlock_radius_m AS unlockRadiusM,
        (
            6371 * acos(
                cos(radians(:userLat)) * cos(radians(e.latitude)) *
                cos(radians(e.longitude) - radians(:userLng)) +
                sin(radians(:userLat)) * sin(radians(e.latitude))
            )
        ) AS distance
    FROM echoes e
    WHERE e.status = :status
      AND e.visibility = :visibility
    ORDER BY distance ASC
    """, nativeQuery = true)
	Slice<EchoPreviewProjection> getEchoPreviews(
			@Param("userLat") double userLat,
			@Param("userLng") double userLng,
			@Param("status") String status,
			@Param("visibility") String visibility,
			Pageable pageable
	);

	@Query("""
			SELECT COUNT(DISTINCT e.id)
						FROM Echo e
						WHERE e.user.id = :userId
			""")
    long countCreatedEchoByUserId(Long userId);

	long countByUser_IdAndCapsule(Long userId, boolean isCapsule);
}
