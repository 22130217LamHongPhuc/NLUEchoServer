package com.be_nlu_echo.service;

import com.be_nlu_echo.dto.request.NearbyCampusLocationRequest;
import com.be_nlu_echo.dto.response.NearbyCampusLocationResponse;
import com.be_nlu_echo.entity.CampusLocation;
import com.be_nlu_echo.repository.CampusLocationRepository;
import com.be_nlu_echo.service.CampusLocationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j

public class CampusLocationService  {

    private final CampusLocationRepository campusLocationRepository;

    public List<NearbyCampusLocationResponse> getNearbyLocations(NearbyCampusLocationRequest request) {

        log.info("=== START getNearbyLocations ===");
        log.info("User location: lat={}, lon={}", request.getLatitude(), request.getLongitude());

        // 1. Lấy dữ liệu từ DB
        List<CampusLocation> locations = campusLocationRepository.findByIsActiveTrue();
        log.info("Total active locations from DB: {}", locations.size());

        // 2. Map + tính khoảng cách
        List<NearbyCampusLocationResponse> mappedList = locations.stream()
                .map(location -> {
                    double distance = calculateDistanceMeters(
                            request.getLatitude(),
                            request.getLongitude(),
                            location.getLatitude(),
                            location.getLongitude()
                    );

                    log.debug("Location: {} | Distance: {} m | Radius: {} m",
                            location.getName(),
                            distance,
                            location.getRadiusMeters()
                    );

                    return NearbyCampusLocationResponse.builder()
                            .id(location.getId())
                            .name(location.getName())
                            .latitude(location.getLatitude())
                            .longitude(location.getLongitude())
                            .radiusMeters(location.getRadiusMeters())
                            .type(location.getType())
                            .priority(location.getPriority())
                            .distanceMeters(distance)
                            .build();
                })
                .toList();

        for(NearbyCampusLocationResponse location : mappedList){
            log.info("Mapped Location: {} | Distance: {} m | Radius: {} m",
                    location.getName(),
                    location.getDistanceMeters(),
                    location.getRadiusMeters()
            );
        }


        // 3. Filter theo bán kính
        List<NearbyCampusLocationResponse> filteredList = mappedList.stream()
                .filter(item -> {
                    boolean isInRange = item.getDistanceMeters() <= item.getRadiusMeters();

                    if (!isInRange) {
                        log.debug("Filtered OUT: {} | distance={} > radius={}",
                                item.getName(),
                                item.getDistanceMeters(),
                                item.getRadiusMeters()
                        );
                    } else {
                        log.debug("Kept: {} | distance={} <= radius={}",
                                item.getName(),
                                item.getDistanceMeters(),
                                item.getRadiusMeters()
                        );
                    }

                    return isInRange;
                })
                .toList();

        log.info("After filtering: {} items", filteredList.size());

        // 4. Sort
        List<NearbyCampusLocationResponse> sortedList = filteredList.stream()
                .sorted(
                        Comparator.comparing(NearbyCampusLocationResponse::getDistanceMeters)
                                .thenComparing(
                                        NearbyCampusLocationResponse::getPriority,
                                        Comparator.reverseOrder()
                                )
                )
                .toList();

        log.info("After sorting: {} items", sortedList.size());

        log.info("=== END getNearbyLocations ===");

        return sortedList;
    }

    private double calculateDistanceMeters(
            double lat1,
            double lon1,
            double lat2,
            double lon2
    ) {
        final double earthRadius = 6371000; // mét

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }
}