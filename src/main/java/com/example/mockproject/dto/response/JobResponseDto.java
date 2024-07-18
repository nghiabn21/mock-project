package com.example.mockproject.dto.response;

import com.example.mockproject.model.Level;
import com.example.mockproject.model.Skill;
import com.example.mockproject.utils.enums.JobStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
@Getter
@Setter
public class JobResponseDto {

    private Integer jobId;

    private String jobTitle;

    private Date startDate;

    private Date endDate;

    private Double salaryFrom;

    private Double salaryTo;

    private String workingAddress;

    private String description;

    private Set<String> skills;
    private Set<String> benefits;
    private Set<String> levels;

    @Enumerated(EnumType.ORDINAL)
    private JobStatusEnum jobStatus;
}
