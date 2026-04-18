package com.be_nlu_echo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NearbyCampusLocationResponse {
    Long id;
    String name;
    Double latitude;
    Double longitude;
    Double radiusMeters;
    String type;
    Integer priority;
    Double distanceMeters;
}