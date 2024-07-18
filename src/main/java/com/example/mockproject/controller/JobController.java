package com.example.mockproject.controller;

import com.example.mockproject.dto.request.JobRequestDto;
import com.example.mockproject.dto.request.JobRequestUpdateDto;
import com.example.mockproject.dto.response.JobResponseDto;
import com.example.mockproject.dto.response.JobView;
import com.example.mockproject.service.JobService;
import com.example.mockproject.service.impl.ExcelService;
import com.example.mockproject.service.impl.JobServiceImpl;
import com.example.mockproject.utils.annotation.OrdinalEnumConstraint;
import com.example.mockproject.utils.annotation.PageableAnnotation;
import com.example.mockproject.utils.annotation.PositiveIntegerValidation;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.enums.JobStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/job")
@Validated
@RequiredArgsConstructor
public class JobController {
    private final ExcelService fileService;
    private final JobService jobService;

    private final JobServiceImpl jobServiceImpl;

    /**
     * This method is used to get Job information according to the data passed in job_title, status and pagination
     *
     * @param job_Title
     * @param status
     * @param page
     * @param size
     * @return ResponseEntity
     */
    @GetMapping("/job-list")
    public ResponseEntity<?> getJob(@RequestParam(required = false, defaultValue = "") String job_Title,
                                    @OrdinalEnumConstraint(enumClass = JobStatusEnum.class,
                                            message = "status must be in [0,1,2,3,4,5,..]")
                                    @RequestParam(required = false, defaultValue = "")  String status,
                                    @PageableAnnotation(message = "page is not true") @RequestParam(name = "page", required = false, defaultValue = "0") String page,
                                    @PageableAnnotation(message = "size is not true") @RequestParam(name = "size", required = false, defaultValue = "10") String size) {
        Page<JobView> jobViews = jobService.CheckJobView(job_Title, status, page, size);
        return ResponseEntity.ok(jobViews);
    }

    /**
     * This method is used to get Job information according to the data passed in job_title, status, skill, level and pagination
     *
     * @param job_Title
     * @param status
     * @param page
     * @param size
     * @return ResponseEntity
     */
    @GetMapping("/get-allJob")
    public ResponseEntity<?> SearchJob_titleAndStatus(@RequestParam(required = false, defaultValue = "") String job_Title,
                                                      @OrdinalEnumConstraint(enumClass = JobStatusEnum.class,
                                                           message = "status must be in [0,1,2,3,4,5,..]")
                                                      @RequestParam(required = false, defaultValue = "")  String status, String key,
                                                      @PageableAnnotation(message = "page is not true") @RequestParam(name = "page", required = false, defaultValue = "0") String page,
                                                      @PageableAnnotation(message = "size is not true") @RequestParam(name = "size", required = false, defaultValue = "10") String size) {
        Page<JobView> jobViews = jobService.CheckJobViewSkillAndLevel(job_Title, status, key, page, size);
        return ResponseEntity.ok(jobViews);
    }
    /**
     * API creates a new Job
     *
     * @param jobRequestDto
     * @return
     */
    @PostMapping("/create-job")
    public ResponseEntity<?> createJob(@Valid @RequestBody JobRequestDto jobRequestDto) {
        jobService.createJob(jobRequestDto);
        return ResponseEntity.ok(Message.MESSAGE_012);
    }

    /**
     * API gets a job detail by id
     *
     * @param idJob
     * @return
     */
    @GetMapping("/get-detail/{id}")
    public ResponseEntity<?> getDetail(@PathVariable("id") Integer idJob) {
        JobResponseDto jobResponseDto = jobService.getDetailJob(idJob);
        return ResponseEntity.ok(jobResponseDto);
    }

    /**
     * This method is used to update a job by id
     *
     * @param jobRequestUpdateDto
     * @param id String id
     * @return ResponseEntity
     */
    @PutMapping("/edit-job/{id}")
    public ResponseEntity<?> updateJob(@Valid @RequestBody JobRequestUpdateDto jobRequestUpdateDto,
                                       @PositiveIntegerValidation(message = "id is not true") @PathVariable String id) {
        return ResponseEntity.ok(jobServiceImpl.updateJob(jobRequestUpdateDto, id));
    }

    /**
     * This method is used to delete logic a job by id,  after execution will change jobStatus status to CLOSED
     *
     * @param id String id
     * @return ResponseEntity
     */
    @PostMapping("/delete-job/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(jobServiceImpl.deleteJob(id));
    }

}
