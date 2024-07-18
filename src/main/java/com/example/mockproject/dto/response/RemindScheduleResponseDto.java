package com.example.mockproject.dto.response;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemindScheduleResponseDto {
  private Integer scheduleId;

  private String email;

  private Date scheduleFrom;

  private Date scheduleTo;

  private String meetingId;

  private String cvAttachment;

  private String fullName;

  private String offerPosition;

  private String account;

}
