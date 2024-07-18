package com.example.mockproject.dto.request;

import com.example.mockproject.utils.annotation.DateAnnotation;
import com.example.mockproject.utils.annotation.DoubleAnotation;
import com.example.mockproject.utils.annotation.OrdinalEnumConstraint;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.enums.JobStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequestUpdateDto {
    private Integer jobId;

    @NotBlank(message = Message.MESSAGE_013)
    @Size(max = 255, message = Message.MESSAGE_034)
    @Pattern(regexp = "[a-zA-Z0-9][a-zA-Z0-9 ]+", message = "jobTitle invalid")
    private String jobTitle;

    @DateAnnotation
    @NotBlank(message = Message.MESSAGE_013)
    private String startDate;

    @DateAnnotation
    @NotBlank(message = Message.MESSAGE_013)
    private String endDate;

    @DoubleAnotation
    private String salaryFrom;

    @NotBlank(message = Message.MESSAGE_013)
    @DoubleAnotation
    private String salaryTo;

    private String workingAddress;

    private String description;

    @NotEmpty(message = Message.MESSAGE_013)
    @Size(max = 6, min = 1, message = "Enter more than 6 skills or No skills entered yet")
    private Set<String> skills;

    @NotEmpty(message = Message.MESSAGE_013)
    @Size(max = 6, message = "Enter more than 6 benefits")
    private Set<String> benefits;

    @NotEmpty(message = Message.MESSAGE_013)
    private Set<String> level;

    @NotNull(message = Message.MESSAGE_013)
    @NotEmpty(message = Message.MESSAGE_013)
    @OrdinalEnumConstraint(enumClass = JobStatusEnum.class, message = "jobStatus is not valid, must be any of enum JobStatusEnum.class ")
    private String jobStatus;
}


