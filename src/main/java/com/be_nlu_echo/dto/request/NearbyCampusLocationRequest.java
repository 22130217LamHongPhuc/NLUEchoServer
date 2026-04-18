package com.be_nlu_echo.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NearbyCampusLocationRequest {

    @NotNull
    @Min(value = -90)
    @Max(value = 90)
    Double latitude;

    @NotNull
    @Min(value = -180)
    @Max(value = 180)
    Double longitude;

    Double gpsAccuracy;
}
