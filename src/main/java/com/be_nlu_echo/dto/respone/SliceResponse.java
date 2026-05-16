package com.be_nlu_echo.dto.respone;

import lombok.*;

import java.util.List;
import java.util.stream.Collector;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SliceResponse<T> {

    private List<T> content;

    private int page;
    private int size;
    private boolean hasNext;

}