package com.be_nlu_echo.dto.respone;


import lombok.Data;

@Data
public class EchoHistorySummaryResponse {
    long totalExploredEchoes;
    long totalLikedEchoes;
    long totalCreateEchos;

}
