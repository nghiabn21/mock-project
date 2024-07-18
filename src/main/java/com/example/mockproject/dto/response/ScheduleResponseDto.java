package com.example.mockproject.dto.response;

import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleResponseDto {

    private String scheduleId;

    private String scheduleTitle;

    private String candidateName;

    private String scheduleFrom;

    private String scheduleTo;

    private String notes;

    private String location;

    private String recruiterOwnerName;

    private String meetingId;

    private String result;

    private String status;

    private Set<String> interviewers = new HashSet<>();

    private String createdBy;

    private String createdDate;

    private String lastModifiedBy;

    private String lastModifiedDate;

}
