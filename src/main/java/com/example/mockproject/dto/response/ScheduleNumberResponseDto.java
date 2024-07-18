package com.example.mockproject.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
public class ScheduleNumberResponseDto {
    private Integer inProgressScheduleNumber;
    private Integer waitingForInterviewScheduleNumber;
}
