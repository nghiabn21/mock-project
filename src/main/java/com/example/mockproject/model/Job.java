package com.example.mockproject.model;

import com.example.mockproject.utils.enums.JobStatusEnum;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Data
public class Job extends Audit<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    private String jobTitle;

    private Date startDate;

    private Date endDate;

    private Double salaryFrom;

    private Double salaryTo;

    private String workingAddress;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "job_skill",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "job_benefit",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "benefit_id"))
    private Set<Benefit> benefits;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "job_level",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "level_id"))
    private Set<Level> levels;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private JobStatusEnum jobStatus;



}
