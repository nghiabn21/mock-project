package com.example.mockproject.dto.response;

import com.example.mockproject.model.Level;
import com.example.mockproject.model.Skill;
import com.example.mockproject.utils.enums.JobStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobView {
    private Integer jobId;
    private String jobTitle;
    private Date startDate;
    private Date endDate;
    private Set<Skill> skills;
    private Set<Level> levels;
    @Enumerated(EnumType.ORDINAL)
    private JobStatusEnum jobStatus;


}