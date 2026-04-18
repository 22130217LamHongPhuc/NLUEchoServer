package com.be_nlu_echo.repository;

import com.be_nlu_echo.entity.CampusLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampusLocationRepository extends JpaRepository<CampusLocation, Long> {
    List<CampusLocation> findByIsActiveTrue();
}