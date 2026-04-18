package com.be_nlu_echo.repository;

import com.be_nlu_echo.entity.Echo;
import com.be_nlu_echo.enums.EchoStatus;
import com.be_nlu_echo.enums.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
