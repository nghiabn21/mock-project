package com.example.mockproject.service;


import com.example.mockproject.dto.request.JobRequestDto;
import com.example.mockproject.dto.request.JobRequestUpdateDto;
import com.example.mockproject.dto.response.JobResponseDto;
import com.example.mockproject.dto.response.JobView;
import com.example.mockproject.utils.enums.JobStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface JobService {

    JobRequestDto createJob(JobRequestDto jobRequestDto);

    JobResponseDto getDetailJob(Integer idJob);

    String updateJob(JobRequestUpdateDto jobRequestUpdateDto, String id);

    String deleteJob(String id);

    Page<JobView> CheckJobView(String job_Title, String status, String page , String size);
    Page<JobView> CheckJobViewSkillAndLevel(String job_Title, String status, String key, String page , String size);
}
