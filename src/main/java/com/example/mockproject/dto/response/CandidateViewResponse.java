package com.example.mockproject.dto.response;

import com.example.mockproject.utils.enums.CandidateStatusEnum;
import com.example.mockproject.utils.enums.PositionEnum;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CandidateViewResponse {
    private Integer candidateId;
    private String recruiter;
    private CandidateStatusEnum candidateStatus;
    private String email;
    private String fullName;
    private String phoneNumber;
    private PositionEnum positionEnum;
}
