package com.example.mockproject.dto.response;

import com.example.mockproject.model.Skill;
import com.example.mockproject.utils.enums.CandidateStatusEnum;
import com.example.mockproject.utils.enums.GenderEnum;
import com.example.mockproject.utils.enums.HighestLevelEnum;
import com.example.mockproject.utils.enums.PositionEnum;
import lombok.Setter;

import java.util.Set;

@Setter
public class CandidateResponseDto {
    private Integer yearOfExperience;
    private Set<String> skills;
    private String note;
    private String position;
    private String candidateStatus;
    private String highestLevel;
    private String fullName;
    private String dob;
    private String phoneNumber;
    private String email;
    private String address;
    private String gender;
    private String account;
}
