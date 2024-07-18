package com.example.mockproject.dto.response;

import com.example.mockproject.model.Skill;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CandidateDropDownResponseDto {
    List<Integer> gender;
    List<Integer> candidateStatus;
    List<Integer> highestLevel;
    List<String> skills;
    List<Integer> position;
    List<String> recruiter;
}
