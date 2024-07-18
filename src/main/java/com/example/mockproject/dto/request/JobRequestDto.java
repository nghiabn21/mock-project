package com.example.mockproject.dto.request;

import com.example.mockproject.utils.annotation.DateAnnotation;
import com.example.mockproject.utils.annotation.DoubleAnotation;
import com.example.mockproject.utils.annotation.OrdinalEnumConstraint;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.enums.JobStatusEnum;
import com.example.mockproject.utils.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequestDto {
    private Integer jobId;

    @NotBlank(message = Message.MESSAGE_013)
    @Size(max = 255, message = Message.MESSAGE_034)
    private String jobTitle;


    @NotBlank(message = Message.MESSAGE_013)
    @DateAnnotation
    private String startDate;

    @DateAnnotation
    @NotBlank(message = Message.MESSAGE_013)
    private String endDate;

    @DoubleAnotation
    private String salaryFrom;

    @NotBlank(message = Message.MESSAGE_013)
    @DoubleAnotation
    private String salaryTo;

    @Size(max = 255, message = "No more than 255 characters")
    private String workingAddress;

    private String description;

    @NotEmpty(message = Message.MESSAGE_013)
    @Size(max = 6,message = "Enter more than 6 skills or No skills entered yet")
    private Set<String> skills;

    @NotEmpty(message = Message.MESSAGE_013)
    @Size(max = 6, message = "Enter more than 6 benefits")
    private Set<String> benefits;

    @NotEmpty(message = Message.MESSAGE_013)
    private Set<String> level;

}

