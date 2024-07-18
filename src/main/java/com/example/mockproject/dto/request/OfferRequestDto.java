package com.example.mockproject.dto.request;

import com.example.mockproject.model.Offer;
import com.example.mockproject.utils.annotation.DoubleAnotation;
import com.example.mockproject.utils.annotation.IntegerValidation;
import com.example.mockproject.utils.annotation.OrdinalEnumConstraint;
import com.example.mockproject.utils.common.DateUtils;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.enums.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferRequestDto {

    @NotBlank(message = Message.MESSAGE_023)
    @IntegerValidation(message = "Must be positive integer")
    private String scheduleId;

    @NotBlank(message = Message.MESSAGE_023)
    @OrdinalEnumConstraint(enumClass = PositionEnum.class,
            message = "position must be in [0,1,2,3,4]")
    private String position;

    @NotBlank(message = Message.MESSAGE_023)
    @IntegerValidation(message = "Must be positive integer")
    private String managerId;

    @NotBlank(message = Message.MESSAGE_023)
    private String contractStart;

    @NotBlank(message = Message.MESSAGE_023)
    private String contractEnd;

    @NotBlank(message = Message.MESSAGE_023)
    @OrdinalEnumConstraint(enumClass = ContractTypeEnum.class,
            message = "contract type must be in [0,1,2,3,4]")
    private String contractType;

    @NotBlank(message = Message.MESSAGE_023)
    @OrdinalEnumConstraint(enumClass = LevelEnum.class,
            message = "level must be in [0,1,2,3,4,5,6,7,8]")
    private String level;

    @NotBlank(message = Message.MESSAGE_023)
    @OrdinalEnumConstraint(enumClass = DepartmentEnum.class,
            message = "department must be in [0,1,2,3,4,5]")
    private String department;

    @NotBlank(message = Message.MESSAGE_023)
    @IntegerValidation(message = "Must be positive integer")
    private String recruiterOwnerId;

    @DateTimeFormat
    @NotBlank(message = Message.MESSAGE_023)
    private String dueDate;

    @DoubleAnotation
    private String basicSalary;

    @Length(max = 255, message = "must be less than 256 characters ")
    private String note;

    @NotBlank(message = Message.MESSAGE_023)
    @OrdinalEnumConstraint(enumClass = OfferStatusEnum.class,
            message = "status must be in [0,1,2,3,4,5,6]")
    private String status;

    public static Offer toEntity(OfferRequestDto requestDto) {
        if (requestDto == null) return null;
        PositionEnum positionEnum = PositionEnum.getByKey(Integer.parseInt(requestDto.getPosition()));
        ContractTypeEnum contractTypeEnum = ContractTypeEnum.getByKey(Integer.parseInt(requestDto.getContractType()));
        LevelEnum levelEnum = LevelEnum.getByKey(Integer.parseInt(requestDto.getLevel()));
        DepartmentEnum departmentEnum = DepartmentEnum.getByKey(Integer.parseInt(requestDto.getDepartment()));

        return Offer.builder()
                .position(positionEnum)
                .contractStart(DateUtils.convertStringToDate(requestDto.getContractStart()))
                .contractEnd(DateUtils.convertStringToDate(requestDto.getContractEnd()))
                .contractType(contractTypeEnum)
                .level(levelEnum)
                .department(departmentEnum)
                .dueDate(DateUtils.convertStringToDate(requestDto.getDueDate()))
                .basicSalary(requestDto.getBasicSalary() == null || requestDto.getBasicSalary().isEmpty() ? 0 : Double.parseDouble(requestDto.getBasicSalary()))
                .note(requestDto.getNote())
                .build();
    }
}
