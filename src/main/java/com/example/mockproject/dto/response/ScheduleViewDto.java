package com.example.mockproject.dto.response;

import lombok.*;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleViewDto {
    private String scheduleId;

    private String scheduleTitle;

    private String candidateName;

    private List<String> interviewerNameList = new ArrayList<>();

    private String scheduleFrom;

    private String scheduleTo;

    private String result;

    private String status;

}
