package com.example.mockproject.service.impl;

import com.example.mockproject.dto.request.JobRequestDto;
import com.example.mockproject.dto.request.JobRequestUpdateDto;
import com.example.mockproject.dto.response.JobResponseDto;
import com.example.mockproject.dto.response.JobView;
import com.example.mockproject.model.Benefit;
import com.example.mockproject.model.Job;
import com.example.mockproject.model.Level;
import com.example.mockproject.model.Skill;
import com.example.mockproject.repository.BenefitRepository;
import com.example.mockproject.repository.JobRepository;
import com.example.mockproject.repository.LevelRepository;
import com.example.mockproject.repository.SkillRepository;
import com.example.mockproject.service.JobService;
import com.example.mockproject.utils.common.DateUtils;
import com.example.mockproject.utils.constant.DateFormat;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.enums.JobStatusEnum;
import com.example.mockproject.utils.exception.ApiNotFoundException;
import com.example.mockproject.utils.exception.ApiRequestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
public class JobServiceImpl implements JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private LevelRepository levelRepository;

    /**
     * This method will update a job by id
     *
     * @param jobRequestUpdateDto
     * @param id
     * @return Job
     */
    @Override
    public String updateJob(JobRequestUpdateDto jobRequestUpdateDto, String id) {

        checkSalary2(jobRequestUpdateDto);
        checkDate(jobRequestUpdateDto.getStartDate(), jobRequestUpdateDto.getEndDate());
        Job job = jobRepository.findById(Integer.valueOf(id)).orElseThrow(() -> new ApiRequestException(Message.ID_JOB_NOT_EXIST));

        try {
            toJob2(job, jobRequestUpdateDto);
            job.setJobStatus(JobStatusEnum.getByKey(Integer.parseInt(jobRequestUpdateDto.getJobStatus())));

            // Set Skill
            Skill skill;
            Set<Skill> skills = new HashSet<>();
            Set<String> skillString = jobRequestUpdateDto.getSkills();
            for (String i : skillString) {
                skill = skillRepository.findByNameIgnoreCase(i);
                if (skill != null) {
                    skills.add(skill);
                } else {
                    skills.add(new Skill(StringUtils.capitalize(i.trim().toLowerCase())));
                }
            }

            // Set Benefit
            Benefit benefit;
            Set<Benefit> benefits = new HashSet<>();
            Set<String> benefitString = jobRequestUpdateDto.getBenefits();
            for (String i : benefitString) {
                benefit = benefitRepository.findByNameIgnoreCase(i);
                if (benefit != null) {
                    benefits.add(benefit);
                } else {
                    benefits.add(new Benefit(StringUtils.capitalize(i.trim().toLowerCase())));
                }
            }

            // Set Level
            Level level;
            Set<Level> levels = new HashSet<>();
            Set<String> levelString = jobRequestUpdateDto.getLevel();
            for (String i : levelString) {
                level = levelRepository.findByNameIgnoreCase(i);
                if (level != null) {
                    levels.add(level);
                } else {
                    levels.add(new Level(StringUtils.capitalize(i.trim().toLowerCase())));
                }
            }

            job.setSkills(skills);
            job.setBenefits(benefits);
            job.setLevels(levels);
            jobRepository.save(job);
        } catch (RuntimeException e) {
            throw new ApiRequestException(Message.MESSAGE_014);
        }
        return Message.MESSAGE_015;
    }

    /**
     * This method will delete logic a job by id, after execution will change jobStatus status to CLOSED
     *
     * @param id String id
     * @return Message String
     */
    @Override
    public String deleteJob(String id) {
        Job job = jobRepository.findById(Integer.valueOf(id)).orElseThrow(() -> new ApiRequestException(Message.ID_JOB_NOT_EXIST));
        if (job.getJobStatus() == JobStatusEnum.CLOSED) {
            throw new ApiRequestException("id has been deleted");
        }
        try {
            job.setJobStatus(JobStatusEnum.CLOSED);
            jobRepository.save(job);
            return Message.MESSAGE_017;
        } catch (Exception e) {
            return Message.MESSAGE_018;
        }
    }


    /**
     * check input data job_title and status :
     * Job_title  and status are empty
     * Job_title is not empty , status is empty
     * Job_title and status are not empty
     *
     * @param job_Title String
     * @param status    String
     * * @param page String
     *   @param size  String
     * @return Page<JobView>
     */
    @Override
    public Page<JobView> CheckJobView(String job_Title, String status, String page , String size) {
        Pageable pageable;
        pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
        Page<Job> jobs;
        Page<JobView> jobViews2;
        if (status.isEmpty()) {
            jobs = jobRepository.findByJobTitleAndAndJobStatus(job_Title, null, pageable);
            if (!jobs.hasContent()) {
                throw new ApiNotFoundException(Message.MESSAGE_038);
            } else {
                jobViews2 = jobs.map(this::toDto);
                return jobViews2;
            }
        } else {
            jobs = jobRepository.findByJobTitleAndAndJobStatus(job_Title, Integer.parseInt(status), pageable);
            if (!jobs.hasContent()) {
                throw new ApiNotFoundException(Message.MESSAGE_038);
            } else {
                jobViews2 = jobs.map(this::toDto);
                return jobViews2;
            }
        }

    }

    /**
     * check input data job_title, status and Skill, level :
     * Job_title  and status are empty
     * Skill  are empty
     * level  are empty
     * Job_title is not empty , status is empty
     * Job_title and status are not empty
     *
     * @param job_Title String
     * @param status    String
     * * @param page String
     *   @param size  String
     * @return Page<JobView>
     */
    @Override
    public Page<JobView> CheckJobViewSkillAndLevel(String job_Title, String status,String key, String page , String size) {
        Pageable pageable;
        pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
        Page<Job> jobs;
        Page<JobView> jobViews2;
        if (status.isEmpty()) {
            jobs = jobRepository.findByJobListAndAndSkillsAndAndLevels(job_Title, null,key, pageable);
            if (!jobs.hasContent()) {
                throw new ApiNotFoundException(Message.MESSAGE_038);
            } else {
                jobViews2 = jobs.map(this::toDto);
                return jobViews2;
            }
        }else {
            jobs = jobRepository.findByJobListAndAndSkillsAndAndLevels(job_Title, Integer.parseInt(status),key , pageable);
            if (!jobs.hasContent()) {
                throw new ApiNotFoundException(Message.MESSAGE_038);
            } else {
                jobViews2 = jobs.map(this::toDto);
                return jobViews2;
            }
        }

    }

    /**
     * The method creats a new Job
     * Serviece
     *
     * @param jobRequestDto
     * @return
     */
    @Override
    public JobRequestDto createJob(JobRequestDto jobRequestDto) {
        checkSalary(jobRequestDto);
        checkDate(jobRequestDto.getStartDate(), jobRequestDto.getEndDate());

        try {
            Job job = new Job();
            toJob(job, jobRequestDto);

            Skill skill;
            Set<Skill> skills = new HashSet<>();
            Set<String> skillString = jobRequestDto.getSkills();
            for (String i : skillString) {
                skill = skillRepository.findByNameIgnoreCase(i.trim());
                if (skill != null) {
                    skills.add(skill);
                } else {
                    skill = new Skill(StringUtils.capitalize(i.trim()));
                    skills.add(skill);
                }
            }

            Benefit benefit;
            Set<Benefit> benefits = new HashSet<>();
            Set<String> benefitString = jobRequestDto.getBenefits();
            for (String x : benefitString) {
                benefit = benefitRepository.findByNameIgnoreCase(x.trim());
                if (benefit != null) {
                    benefits.add(benefit);
                } else {
                    benefit = new Benefit(StringUtils.capitalize(x.trim().toLowerCase()));
                    benefits.add(benefit);
                }
            }

            Level level;
            Set<Level> levels = new HashSet<>();
            Set<String> levelString = jobRequestDto.getLevel();
            for (String x : levelString) {
                level = levelRepository.findByNameIgnoreCase(x.trim());
                if (level != null) {
                    levels.add(level);
                } else {
                    level = new Level(StringUtils.capitalize(x.trim()));
                    levels.add(level);
                }
            }
            job.setSkills(skills);
            job.setBenefits(benefits);
            job.setLevels(levels);
            job.setJobStatus(JobStatusEnum.OPEN);
            jobRepository.save(job);
            return jobRequestDto;
        } catch (ApiRequestException e) {
            throw new ApiRequestException(Message.MESSAGE_011);
        }
    }


    /**
     * The method gets a job detail
     *
     * @param idJob
     * @return
     */
    @Override
    public JobResponseDto getDetailJob(Integer idJob) {
        try {
            Job job = jobRepository.findById(idJob).orElseThrow(
                    () -> new ApiRequestException(Message.MESSAGE_035));
            JobResponseDto jobResponseDto = new JobResponseDto();

            jobResponseDto.setJobId(job.getJobId());
            jobResponseDto.setJobTitle(job.getJobTitle());
            jobResponseDto.setStartDate(job.getStartDate());
            jobResponseDto.setEndDate(job.getEndDate());
            jobResponseDto.setSalaryFrom(job.getSalaryFrom());
            jobResponseDto.setSalaryTo(job.getSalaryTo());
            jobResponseDto.setDescription(jobResponseDto.getDescription());
            jobResponseDto.setWorkingAddress(job.getWorkingAddress());

            Set<String> skill = new HashSet<>();
            job.getSkills().forEach(s -> skill.add(s.getName()));
            jobResponseDto.setSkills(skill);

            Set<String> benefits = new HashSet<>();
            job.getBenefits().forEach(s -> benefits.add(s.getName()));
            jobResponseDto.setBenefits(benefits);

            Set<String> levels = new HashSet<>();
            job.getLevels().forEach(s -> levels.add(s.getName()));
            jobResponseDto.setLevels(levels);
            jobResponseDto.setJobStatus(job.getJobStatus());
            return jobResponseDto;
        } catch (Exception e) {
            throw new ApiRequestException(Message.MESSAGE_035);
        }
    }







    /**
     * This function will delete logic a job by id, after execution will change jobStatus status to CLOSED
     *
     * @param job
     * @return ResponseEntity
     */
    public JobView toDto(Job job) {
        JobView jobResponseDto = new JobView();
        jobResponseDto.setJobId(job.getJobId());
        jobResponseDto.setJobTitle(job.getJobTitle());
        jobResponseDto.setSkills(job.getSkills());
        jobResponseDto.setLevels(job.getLevels());
        jobResponseDto.setStartDate(job.getStartDate());
        jobResponseDto.setEndDate(job.getEndDate());
        jobResponseDto.setJobStatus(job.getJobStatus());
        return jobResponseDto;
    }

    /**
     * The function check condition for date
     * The startDate must be greater than the current date
     * The endDate must be greater than the start date
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public void checkDate(String startDate, String endDate) {
        LocalDate localStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern(DateFormat.DATETIME));
        LocalDate localEndDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern(DateFormat.DATETIME));
        if (localStartDate.isAfter(LocalDate.now())) {
            if (!localEndDate.isAfter(localStartDate)) {
                throw new ApiRequestException("The end date must be greater than the start date");
            }
        } else {
            throw new ApiRequestException("The start date is in the wrong format");
        }
    }

    /**
     * The function check condition for salary
     *
     * @param jobRequestDto
     */
    public void checkSalary(JobRequestDto jobRequestDto) {
        if (!"".equals(jobRequestDto.getSalaryFrom())) {
            if (Objects.nonNull(jobRequestDto.getSalaryFrom())) {
                if (!(Double.parseDouble(jobRequestDto.getSalaryTo()) > Double.parseDouble(jobRequestDto.getSalaryFrom()))) {
                    throw new ApiRequestException("The value of <Salary to> must be greater than <Salary from>");
                }
            }
        }
    }

    /**
     * The function check condition for salary
     *
     * @param jobRequestUpdateDto
     */
    public void checkSalary2(JobRequestUpdateDto jobRequestUpdateDto) {
        if (!"".equals(jobRequestUpdateDto.getSalaryFrom())) {
            if (Objects.nonNull(jobRequestUpdateDto.getSalaryFrom())) {
                if (!(Double.parseDouble(jobRequestUpdateDto.getSalaryTo()) > Double.parseDouble(jobRequestUpdateDto.getSalaryFrom()))) {
                    throw new ApiRequestException("The value of <Salary to> must be greater than <Salary from>");
                }
            }
        }
    }

    /**
     * Convert JobRequestDto to Job
     *
     * @param job
     * @param jobRequestDto
     */
    private void toJob(Job job, JobRequestDto jobRequestDto) {
        job.setJobTitle(StringUtils.capitalize(jobRequestDto.getJobTitle().trim()));
        job.setStartDate(DateUtils.convertStringToDate(jobRequestDto.getStartDate()));
        job.setEndDate(DateUtils.convertStringToDate(jobRequestDto.getEndDate()));
        if ("".equals(jobRequestDto.getSalaryFrom())) {
            job.setSalaryFrom(null);
        } else if (Objects.nonNull(jobRequestDto.getSalaryFrom())) {
            job.setSalaryFrom(Double.parseDouble(jobRequestDto.getSalaryFrom()));
        }
        job.setSalaryTo(Double.parseDouble(jobRequestDto.getSalaryTo()));
        job.setWorkingAddress(StringUtils.capitalize(jobRequestDto.getWorkingAddress()));
        job.setDescription(StringUtils.capitalize(jobRequestDto.getDescription()));
    }

    /**
     * Convert JobRequestUpdateDto to Job
     *
     * @param job
     * @param jobRequestUpdateDto
     */
    private void toJob2(Job job, JobRequestUpdateDto jobRequestUpdateDto) {
        job.setJobTitle(StringUtils.capitalize(jobRequestUpdateDto.getJobTitle().trim()));
        job.setStartDate(DateUtils.convertStringToDate(jobRequestUpdateDto.getStartDate()));
        job.setEndDate(DateUtils.convertStringToDate(jobRequestUpdateDto.getEndDate()));
        if ("".equals(jobRequestUpdateDto.getSalaryFrom())) {
            job.setSalaryFrom(null);
        } else if (Objects.nonNull(jobRequestUpdateDto.getSalaryFrom())) {
            job.setSalaryFrom(Double.parseDouble(jobRequestUpdateDto.getSalaryFrom()));
        }
        job.setSalaryTo(Double.parseDouble(jobRequestUpdateDto.getSalaryTo()));
        job.setWorkingAddress(StringUtils.capitalize(jobRequestUpdateDto.getWorkingAddress()));
        job.setDescription(StringUtils.capitalize(jobRequestUpdateDto.getDescription()));
    }
}
