package com.example.mockproject.dto.request;

import com.example.mockproject.utils.annotation.*;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.enums.*;
import com.example.mockproject.utils.validation.Validation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
public class CandidateRequestDto {
    @YearOfExperience(message = "yearOfExperience is not true")
    private String yearOfExperience;

    @NotEmpty(message = Message.MESSAGE_003)
    private Set<String> skills;

    @Size(max = 500)
    private String note;

    @NotBlank(message = Message.MESSAGE_003)
    @Enumerated(EnumType.ORDINAL)
    @OrdinalEnumConstraint(enumClass = PositionEnum.class,
            message = "position is not true")
    private String positionEnum;

    @NotBlank(message = Message.MESSAGE_003)
    @Enumerated(EnumType.ORDINAL)
    @OrdinalEnumConstraint(enumClass = CandidateStatusEnum.class,
            message = "status  is not true")
    private String candidateStatus;

    @NotBlank(message = Message.MESSAGE_003 )
    @Enumerated(EnumType.ORDINAL)
    @OrdinalEnumConstraint(enumClass = HighestLevelEnum.class,
            message = "highestLevel is not true")
    private String highestLevel;

    @NotBlank(message = Message.MESSAGE_003)
    @Pattern(regexp =Validation.NAME_PATTERN,message = "Full name is not valid")
    @Size(max = 255)
    private String fullName;

    @DateAnnotation
    private String dob;

    @PhoneAnnotation
    private String phoneNumber;

    @NotBlank(message = Message.MESSAGE_003)
    @Email(message = "Unformatted email")
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String address;

    @NotBlank(message = Message.MESSAGE_003 )
    @Enumerated(EnumType.ORDINAL)
    @OrdinalEnumConstraint(enumClass = GenderEnum.class,
            message = "gender  is not true")
    private String gender;

    @NotBlank(message = Message.MESSAGE_003)
    @Size(max = 255)
    private String account;


}
