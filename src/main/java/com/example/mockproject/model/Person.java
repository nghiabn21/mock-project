package com.example.mockproject.model;

import com.example.mockproject.utils.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person extends Audit<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer personId;

    private String fullName;

    private Date dob;

    private String phoneNumber;

    private String email;

    private String address;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "gender")
    private GenderEnum gender;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "person")
    @JsonBackReference
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER,mappedBy = "person")
    @JsonBackReference
    private Candidate candidate;
}
