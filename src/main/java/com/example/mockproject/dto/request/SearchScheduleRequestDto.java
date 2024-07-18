package com.example.mockproject.dto.request;

import com.example.mockproject.utils.annotation.ValidIntegerList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchScheduleRequestDto {
    private String title = "";

    @Size(max = 4, message = "interviewerIds must be equal or less than 4 element")
    @ValidIntegerList
    private List<String> interviewerIds = new ArrayList<>();

    @Size(max = 20, message = "statusList must be equal or less than 20 element")
    @ValidIntegerList
    private List<String> statusList = new ArrayList<>();
}
