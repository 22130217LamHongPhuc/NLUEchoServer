package com.be_nlu_echo.repository;

import com.be_nlu_echo.dto.projection.UserLevelSummaryProjection;
import com.be_nlu_echo.entity.UserLevelSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserLevelSummaryRepository extends JpaRepository<UserLevelSummary, Long> {

    Optional<UserLevelSummary> findByUserId(Long userId);

}
