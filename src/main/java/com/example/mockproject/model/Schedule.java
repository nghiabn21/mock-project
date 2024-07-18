package com.example.mockproject.model;

import com.example.mockproject.utils.enums.ScheduleResultEnum;
import com.example.mockproject.utils.enums.ScheduleStatusEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule extends Audit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleId;

    private String scheduleTitle;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    @JsonManagedReference
    @ToString.Exclude
    private Candidate candidate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduleFrom;

    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduleTo;

    private String notes;

    private String location;

    @OneToOne
    @JoinColumn(name = "recruiter_owner_id", nullable = false)
    @ToString.Exclude
    private User recruiterOwner;

    private String meetingId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "result")
    private ScheduleResultEnum result;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private ScheduleStatusEnum scheduleStatus;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "schedule_interviewers",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "interviewer_id"))
    private Set<User> interviewers = new HashSet<>();

}
