package com.example.mockproject.model;

import com.example.mockproject.utils.enums.DepartmentEnum;
import com.example.mockproject.utils.enums.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends Audit<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "account", nullable = false, unique = true)
    private String account;

    @Column(name = "password")
    private String password;

    @Column(name = "temporary_password")
    private String temporaryPassword;

    @Column(name = "reset_password_token")
    private String resetPasswordToken ;

    private Date expiryDate;

    @Column(name = "department")
    @Enumerated(EnumType.ORDINAL)
    private DepartmentEnum department;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private UserStatusEnum status;

    @Column(name = "notes")
    private String note;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", nullable = false)
    @JsonManagedReference
    private Person person;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles;

    @OneToMany(mappedBy = "recruiter")
    @JsonBackReference
    private Set<Candidate> candidates;

    @OneToMany(mappedBy = "manager")
    @JsonBackReference
    private Set<Offer> offers;
}
