package com.example.mockproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataCreateScheduleResponseDto {
    List<Object> candidateList;
    List<Object> interviewerList;
    List<Object> recruiterList;
}
