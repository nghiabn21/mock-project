package com.example.mockproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CandidateInformationResponseDto {
    private String fullName;
    private String dob;
    private String phoneNumber;
    private String email;
    private String address;
    private String gender;
    private String cvAttachment;
    private String Position;
    private Set<String> skills;
    private String recruiter;
    private String status;
    private int yearOfExperience;
    private String highestLevel;
    private String note;
    private String createdDate;
    private String lastModifiedDate;
    private String lastModifiedBy;
    private String link;
}
