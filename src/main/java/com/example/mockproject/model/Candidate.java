package com.example.mockproject.model;

import com.example.mockproject.utils.enums.CandidateStatusEnum;
import com.example.mockproject.utils.enums.HighestLevelEnum;
import com.example.mockproject.utils.enums.PositionEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidate extends Audit<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer candidateId;

    private String cvAttachment;

    private Integer yearOfExperience;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "candidate_skill",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills;

    @Column(length = 500)
    private String note;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    @JsonManagedReference
    private Person person;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recruiter_id")
    @JsonManagedReference
    private User recruiter;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "position")
    private PositionEnum positionEnum;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private CandidateStatusEnum candidateStatus;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "highest_level")
    private HighestLevelEnum highestLevel;

    @OneToMany(mappedBy = "candidate")
    @JsonBackReference
    private Set<Schedule> scheduleSet;

    public Candidate(Integer candidateId, Person person, User recruiter, CandidateStatusEnum candidateStatus) {
        this.candidateId = candidateId;
        this.person = person;
        this.recruiter = recruiter;
        this.candidateStatus = candidateStatus;
    }
}
