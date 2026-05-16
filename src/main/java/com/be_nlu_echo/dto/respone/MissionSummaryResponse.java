package com.be_nlu_echo.dto.respone;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionSummaryResponse {

    private Long inProgress;
    private Long claimable;
    private Long completed;
}