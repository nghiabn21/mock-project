package com.example.mockproject.model;

import com.example.mockproject.utils.enums.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Offer  extends Audit<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer offerId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date contractStart;

    @Temporal(TemporalType.TIMESTAMP)
    private Date contractEnd;

    private String note;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "contract_type")
    private ContractTypeEnum contractType;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "level")
    private LevelEnum level;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "department")
    private DepartmentEnum department;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "recruiter_owner_id")
    private User recruiterOwner;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;

    private Double basicSalary;

    @Enumerated(EnumType.ORDINAL)
    private PositionEnum position;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    @JsonManagedReference
    private User manager;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    //add field OfferStatusEnum
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "offer_status")
    private OfferStatusEnum offerStatusEnum;

}
