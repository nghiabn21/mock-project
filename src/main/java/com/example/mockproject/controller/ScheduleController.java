package com.example.mockproject.controller;

import com.example.mockproject.dto.request.ScheduleRequestDto;
import com.example.mockproject.dto.request.SearchScheduleRequestDto;
import com.example.mockproject.dto.response.DataCreateScheduleResponseDto;
import com.example.mockproject.dto.response.MessageResponseDto;
import com.example.mockproject.dto.response.ScheduleNumberResponseDto;
import com.example.mockproject.dto.response.ScheduleViewDto;
import com.example.mockproject.service.ScheduleService;
import com.example.mockproject.utils.annotation.IntegerValidation;
import com.example.mockproject.utils.annotation.PageableAnnotation;
import com.example.mockproject.utils.annotation.PositiveIntegerValidation;
import com.example.mockproject.utils.constant.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * This api use to get information details of one interview schedule
     *
     * @param id is id of interview schedule
     * @return ResponseEntity
     */
    @GetMapping("/view-interview-schedule/{id}")
    public ResponseEntity<?> getScheduleDetail(@PositiveIntegerValidation(message = Message.MESSAGE_049) @PathVariable String id) {
        return ResponseEntity.ok(scheduleService.getOneSchedule(Integer.parseInt(id)));
    }

    /**
     * This api use to update fields "result" and "status" interview schedule
     *
     * @param id is id of interview schedule
     * @param scheduleResult is result of interview schedule
     * @return
     */
    @PostMapping("/view-interview-schedule/submit/{id}")
    public ResponseEntity<?> submitResult(@PositiveIntegerValidation(message = Message.MESSAGE_049)
                                          @PathVariable String id,
                                          @NotNull(message = Message.MESSAGE_053)
                                          @IntegerValidation(message = Message.MESSAGE_054)
                                          @RequestParam(required = false) String scheduleResult) {
        return ResponseEntity.ok(scheduleService.updateResultSchedule(Integer.parseInt(id), Integer.parseInt(scheduleResult)));
    }

    /**
     * This api will return a page of Schedule and will be filtered by optional parameters below
     *
     * @param searchScheduleRequestDto is request body contain search parameter for filtering schedule
     * @param pageable use to get pageSize and pageNumber for pagination
     * @return ResponseEntity
     */
    @PostMapping("/interview-schedule-list-grid")
    public ResponseEntity<?> filterScheduleList(@RequestBody @Valid SearchScheduleRequestDto searchScheduleRequestDto,
                                                @PageableDefault Pageable pageable){
        Page<ScheduleViewDto> schedulePage = scheduleService.getSchedulePage(searchScheduleRequestDto, pageable);
        return ResponseEntity.ok(schedulePage);
    }

    /**
     * This api use to get current waiting for interview and in-progress interviewer schedule
     *
     * @param interviewerId is id of current interviewer
     * @return ResponseEntity
     */
    @GetMapping("/get-interviewer-schedule/{interviewerId}")
    public ResponseEntity<?> getCurrentUserInterview(@PathVariable Integer interviewerId) {
        ScheduleNumberResponseDto response = scheduleService.getCurrentInterviewSchedule(interviewerId);
        return ResponseEntity.ok(response);
    }

    /**
     * This api use to create new Schedule
     *
     * @param scheduleRequestDto is request body to convert to entity and save to database
     * @return ResponseEntity
     */
    @PostMapping("/create-interview-schedule")
    @Transactional
    public ResponseEntity<?> createInterviewSchedule(@RequestBody @Valid ScheduleRequestDto scheduleRequestDto) {
        MessageResponseDto responseDto = scheduleService.createInterviewSchedule(scheduleRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * This api use to get data to create new Schedule
     *
     * @return ResponseEntity
     */
    @GetMapping("/create-interview-schedule")
    public ResponseEntity<?> getDataToCreateSchedule() {
        DataCreateScheduleResponseDto responseDto = scheduleService.getDataCreateSchedule();
        return ResponseEntity.ok(responseDto);
    }


    @PutMapping("/schedule/edit-interview-schedule/{scheduleId}")
    public ResponseEntity<?> updateInterviewSchedule(@PositiveIntegerValidation(message = Message.MESSAGE_090) @PathVariable String scheduleId,@Valid  @RequestBody ScheduleRequestDto requestDto) {
        return ResponseEntity.ok().body(scheduleService.updateSchedule(Integer.parseInt(scheduleId), requestDto));
    }
}
