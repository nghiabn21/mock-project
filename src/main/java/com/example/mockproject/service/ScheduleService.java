package com.example.mockproject.service;

import com.example.mockproject.dto.request.ScheduleRequestDto;
import com.example.mockproject.dto.request.SearchScheduleRequestDto;
import com.example.mockproject.dto.response.*;
import com.example.mockproject.model.Schedule;
import com.example.mockproject.utils.enums.ScheduleStatusEnum;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {
    ScheduleResponseDto convertToDto(Schedule schedule);

    Schedule convertToEntity(ScheduleRequestDto scheduleRequestDto);

    ScheduleResponseDto getOneSchedule(Integer id);

    MessageResponseDto updateResultSchedule(Integer id, Integer scheduleResult);

    Page<ScheduleViewDto> getSchedulePage(SearchScheduleRequestDto scheduleRequestDto, Pageable pageable);

    ScheduleNumberResponseDto getCurrentInterviewSchedule(Integer interviewerId);

    MessageResponseDto createInterviewSchedule(ScheduleRequestDto scheduleRequestDto);

    MessageResponseDto updateSchedule(Integer id, ScheduleRequestDto requestDto);

    void updateScheduleStatusEveryday();


    DataCreateScheduleResponseDto getDataCreateSchedule();


    List<Object> getScheduleListByStatus(ScheduleStatusEnum scheduleStatusEnum);

    List<Schedule> getAllScheduleDateFrom();

    void sendScheduleRemind(List<Schedule> list);

    RemindScheduleResponseDto convertToDtoSchedule(Schedule schedule);

}
