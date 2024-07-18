package com.example.mockproject.dto.request;

import com.example.mockproject.utils.annotation.IntegerValidation;
import com.example.mockproject.utils.annotation.OrdinalEnumConstraint;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.enums.ScheduleStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequestDto {
  private Integer scheduleId;

  @Length(max = 255, message = "Must less than 256 characters")
  @NotBlank(message = Message.MESSAGE_023)
  private String scheduleTitle;

  @NotBlank(message = Message.MESSAGE_023)
  @IntegerValidation(message = "Must be positive integer")
  private String candidateId;

  @NotBlank(message = Message.MESSAGE_023)
  private String scheduleFrom;

  @NotBlank(message = Message.MESSAGE_023)
  private String scheduleTo;

  @NotEmpty(message = Message.MESSAGE_023)
  @Size(message = "You only can select maximum 4 interviewer", max = 4)
  private Set<@IntegerValidation(message = "Must be positive integer") String> interviewer = new HashSet<>();

  @Length(max = 255, message = "Must less than 256 characters")
  private String location;

  @NotBlank(message = Message.MESSAGE_023)
  @IntegerValidation(message = "Must be positive integer")
  private String recruiterOwnerId;

  @Length(max = 255, message = "Must less than or equal 256 characters")
  private String note;

  @NotBlank(message = Message.MESSAGE_023)
  @Enumerated(EnumType.ORDINAL)
  @OrdinalEnumConstraint(enumClass = ScheduleStatusEnum.class,
          message = "status must be in [0,1,2,3,4,5]")
  private String status;

  @Length(max = 255, message = "Must less than 256 characters")
  private String meetingId;

}
