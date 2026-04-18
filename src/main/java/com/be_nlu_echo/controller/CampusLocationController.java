package com.be_nlu_echo.controller;

import com.be_nlu_echo.dto.request.NearbyCampusLocationRequest;
import com.be_nlu_echo.dto.respone.ApiResponse;
import com.be_nlu_echo.dto.response.NearbyCampusLocationResponse;
import com.be_nlu_echo.enums.StatusCode;
import com.be_nlu_echo.service.CampusLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campus-locations")
@RequiredArgsConstructor
public class CampusLocationController {

    private final CampusLocationService campusLocationService;

    @GetMapping("/nearby")
    public ApiResponse<NearbyCampusLocationResponse> getNearbyLocations(
            @Valid @ModelAttribute NearbyCampusLocationRequest request
    ) {
        List<NearbyCampusLocationResponse> data = campusLocationService.getNearbyLocations(request);

        return new ApiResponse<>(
                true,
                StatusCode.SUCCESS,
                "Nearby campus locations retrieved successfully",
                data.get(0)
        );

    }


}