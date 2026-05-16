package com.be_nlu_echo.repository;



import com.be_nlu_echo.entity.Mission;
import com.be_nlu_echo.enums.MissionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findByActiveTrue();

    List<Mission> findByCategoryAndActiveTrue(MissionCategory category);
}