package com.example.mockproject.service.impl;

import com.example.mockproject.dto.request.ScheduleRequestDto;
import com.example.mockproject.dto.request.SearchScheduleRequestDto;
import com.example.mockproject.dto.response.DataCreateScheduleResponseDto;
import com.example.mockproject.dto.response.DataMailDto;
import com.example.mockproject.dto.response.MessageResponseDto;
import com.example.mockproject.dto.response.RemindScheduleResponseDto;
import com.example.mockproject.dto.response.ScheduleNumberResponseDto;
import com.example.mockproject.dto.response.ScheduleResponseDto;
import com.example.mockproject.dto.response.ScheduleViewDto;
import com.example.mockproject.model.Candidate;
import com.example.mockproject.model.Schedule;
import com.example.mockproject.model.User;
import com.example.mockproject.repository.CandidateRepository;
import com.example.mockproject.repository.ScheduleRepository;
import com.example.mockproject.service.CandidateService;
import com.example.mockproject.service.EmailService;
import com.example.mockproject.service.ScheduleService;
import com.example.mockproject.service.UserService;
import com.example.mockproject.utils.common.CustomPageUtils;
import com.example.mockproject.utils.common.DateUtils;
import com.example.mockproject.utils.common.FileUtils;
import com.example.mockproject.utils.constant.DateFormat;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.enums.*;
import com.example.mockproject.utils.exception.ApiNotFoundException;
import com.example.mockproject.utils.exception.ApiRequestException;
import java.io.File;
import java.io.FilenameFilter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Buffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final CandidateRepository candidateRepository;

    private final CandidateService candidateService;

    private final UserService userService;

    private final EmailService emailService;
    private final String pathValue ="D:\\";

    /**
     * This method use to convert Schedule to ScheduleResponseDto
     *
     * @param schedule is entity to convert to dto
     * @return ScheduleResponseDto
     */
    @Override
    public ScheduleResponseDto convertToDto(Schedule schedule) {
        Set<String> interviewerAccountStr = new HashSet<>();
        schedule.getInterviewers().forEach(interviewer -> interviewerAccountStr.add(interviewer.getAccount()));

        return ScheduleResponseDto.builder()
                .scheduleId(schedule.getScheduleId().toString())
                .scheduleTitle(schedule.getScheduleTitle())
                .candidateName(schedule.getCandidate().getPerson().getFullName())
                .scheduleFrom(DateUtils.convertDateToString(schedule.getScheduleFrom()))
                .scheduleTo(DateUtils.convertDateToString(schedule.getScheduleTo()))
                .notes(schedule.getNotes())
                .location(schedule.getLocation())
                .recruiterOwnerName(schedule.getRecruiterOwner().getPerson().getFullName())
                .meetingId(schedule.getMeetingId())
                .result(schedule.getResult().getValue())
                .status(schedule.getScheduleStatus().getValue())
                .interviewers(interviewerAccountStr)
                .createdBy(schedule.getCreatedBy())
                .createdDate(DateUtils.convertDateToString(schedule.getCreatedDate()))
                .lastModifiedBy(schedule.getLastModifiedBy())
                .lastModifiedDate(DateUtils.convertDateToString(schedule.getLastModifiedDate()))
                .build();
    }

    /**
     * This method use to convert Schedule to ScheduleViewDto
     *
     * @param schedule is entity to convert to dto
     * @return ScheduleViewDto
     */
    public ScheduleViewDto convertToScheduleViewDto(Schedule schedule) {
        List<String> interviewerNameList = new ArrayList<>();
        schedule.getInterviewers().forEach(interviewer -> interviewerNameList.add(interviewer.getPerson().getFullName()));

        return ScheduleViewDto.builder()
                .scheduleId(schedule.getScheduleId().toString())
                .scheduleTitle(schedule.getScheduleTitle())
                .candidateName(schedule.getCandidate().getPerson().getFullName())
                .scheduleFrom(DateUtils.convertDateToString(schedule.getScheduleFrom()))
                .scheduleTo(DateUtils.convertDateToString(schedule.getScheduleTo()))
                .result(schedule.getResult().getValue())
                .status(schedule.getScheduleStatus().getValue())
                .interviewerNameList(interviewerNameList)
                .build();
    }

    /**
     * This method use to convert scheduleRequestDto to Schedule
     *
     * @param scheduleRequestDto is dto use to convert to entity Schedule
     * @return Schedule
     */
    @Override
    public Schedule convertToEntity(ScheduleRequestDto scheduleRequestDto) {

        Candidate candidate = candidateRepository.findById(Integer.parseInt(scheduleRequestDto.getCandidateId()))
                .orElseThrow(() -> new ApiRequestException(Message.MESSAGE_084));
        if (candidate.getCandidateStatus().equals(CandidateStatusEnum.BANNED)) {
            throw new ApiRequestException(Message.MESSAGE_085);
        }

        User recruiterOwner = userService.getUserHasRole(
                Integer.parseInt(scheduleRequestDto.getRecruiterOwnerId()), RoleEnum.ROLE_RECRUITER);

        List<User> userList = new ArrayList<>();
        for (String interviewerId : scheduleRequestDto.getInterviewer()) {
            User interviewer = userService.getUserHasRole(Integer.parseInt(interviewerId), RoleEnum.ROLE_INTERVIEWER);
            userList.add(interviewer);
        }
        Set<User> interviewerSet = new HashSet<>(userList);
        return Schedule.builder()
                .scheduleTitle(scheduleRequestDto.getScheduleTitle())
                .candidate(candidate)
                .scheduleFrom(DateUtils.convertStringToDate(scheduleRequestDto.getScheduleFrom()))
                .scheduleTo(DateUtils.convertStringToDate(scheduleRequestDto.getScheduleTo()))
                .notes(scheduleRequestDto.getNote())
                .location(scheduleRequestDto.getLocation())
                .meetingId(scheduleRequestDto.getMeetingId())

                .recruiterOwner(recruiterOwner)
                .scheduleStatus(ScheduleStatusEnum.getByKey(Integer.parseInt(scheduleRequestDto.getStatus())))
                .interviewers(interviewerSet)
                .build();

    }

    /**
     * This method use to get one Schedule details by scheduleId
     *
     * @param id is ID of Schedule
     * @return scheduleResponseDto
     */
    @Override
    public ScheduleResponseDto getOneSchedule(Integer id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(Message.MESSAGE_050));
        ScheduleResponseDto scheduleResponseDto = convertToDto(schedule);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date today = new Date();
        String todayStr = dateFormatter.format(today);
        String createdDateStr = dateFormatter.format(schedule.getCreatedDate());
        String modifyDateStr = dateFormatter.format(schedule.getLastModifiedDate());
        scheduleResponseDto.setCreatedDate(todayStr.equals(createdDateStr) ? "today" : createdDateStr);
        scheduleResponseDto.setLastModifiedDate(todayStr.equals(modifyDateStr) ? "today" : modifyDateStr);
        return scheduleResponseDto;
    }

    /**
     * This method use to modify field result, status int Schedule
     *
     * @param id             is ID of Schedule need modify
     * @param scheduleResult is key of ScheduleResultEnum need modify
     * @return MessageResponseDto
     */
    @Override
    @Transactional
    public MessageResponseDto updateResultSchedule( Integer id, Integer scheduleResult) {

        if (scheduleResult == null) {
            return new MessageResponseDto(Message.MESSAGE_053, null, null);
        }
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);
        if (optionalSchedule.isEmpty()) {
            throw new ApiRequestException(Message.MESSAGE_051);
        }
        Schedule schedule = optionalSchedule.get();
        if (schedule.getResult().getKey() == ScheduleResultEnum.FAIL.getKey()
                || schedule.getResult().getKey() == ScheduleResultEnum.PASS.getKey()) {
            return new MessageResponseDto(Message.MESSAGE_052, null, null);
        }
            switch (scheduleResult) {
                case 1:
                    schedule.setResult(ScheduleResultEnum.FAIL);
                    schedule.setScheduleStatus(ScheduleStatusEnum.FAILED_INTERVIEW);
                    break;
                case 2:
                    schedule.setResult(ScheduleResultEnum.PASS);
                    schedule.setScheduleStatus(ScheduleStatusEnum.PASSED_INTERVIEW);
                    break;
                default :
                    throw new ApiRequestException(Message.MESSAGE_054);
            }
            scheduleRepository.save(schedule);
            return new MessageResponseDto(Message.MESSAGE_055, null, null);
        }

    /**
     * This method use to get page Schedule and filter by title, interviewer and status and all param to filter is optional
     *
     * @param searchScheduleRequestDto is object contain filter key like title, interviewerIds, statusList to search Schedule
     * @param pageable use to get pageSize and pageNumber
     * @return Page<Schedule>
     */
    @Override
    public Page<ScheduleViewDto> getSchedulePage(SearchScheduleRequestDto searchScheduleRequestDto, Pageable pageable) {
        if (pageable.getPageNumber() * pageable.getPageSize() < 0) {
            throw new ApiRequestException(Message.MESSAGE_089);
        }
        pageable = CustomPageUtils.getPageable(pageable, "schedule_id", CustomPageUtils.DESC);
        String title = searchScheduleRequestDto.getTitle();
        List<Integer> interviewerIds = searchScheduleRequestDto.getInterviewerIds().stream().map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> statusList = searchScheduleRequestDto.getStatusList().stream().map(Integer::parseInt).collect(Collectors.toList());

        try {
            Page<Schedule> schedulePage = scheduleRepository.
                    findAllByTitleAndInterviewerAndStatus(title, interviewerIds, statusList, pageable);
            Page<ScheduleViewDto> responseDtos = schedulePage.map(this::convertToScheduleViewDto);
            return responseDtos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * This method use to get current interviewer's schedule
     *
     * @param interviewerId is id of current interviewer logged in
     * @return ScheduleNumberResponseDto
     */
    @Override
    public ScheduleNumberResponseDto getCurrentInterviewSchedule(Integer interviewerId) {
        if (!userService.checkUserHasRole(interviewerId, RoleEnum.ROLE_INTERVIEWER)) {
            throw new ApiRequestException(Message.MESSAGE_083);
        }

        int currentInProgressNumber = scheduleRepository
                .countCurUserScheByStatus(interviewerId, ScheduleStatusEnum.IN_PROGRESS.getKey());
        int currentWaitingForInterviewNumber = scheduleRepository
                .countCurUserScheByStatus(interviewerId, ScheduleStatusEnum.WAITING_FOR_INTERVIEW.getKey());

        return ScheduleNumberResponseDto.builder()
                .inProgressScheduleNumber(currentInProgressNumber)
                .waitingForInterviewScheduleNumber(currentWaitingForInterviewNumber)
                .build();
    }

    /**
     * This method use to create new interview schedule
     *
     * @param scheduleRequestDto is requestBody use to get information of new interview schedule
     * @return MessageResponseDto for response message and interview schedule after created
     */
    @Override
    public MessageResponseDto createInterviewSchedule(ScheduleRequestDto scheduleRequestDto) {
        Date scheduleFrom = DateUtils.convertStringToDate(scheduleRequestDto.getScheduleFrom());
        Date scheduleTo = DateUtils.convertStringToDate(scheduleRequestDto.getScheduleTo());
        Integer candidateId = Integer.parseInt(scheduleRequestDto.getCandidateId());
        if (scheduleFrom.before(new Date())) {
            throw new ApiRequestException(Message.MESSAGE_080);
        }
        if (scheduleFrom.after(scheduleTo)) {
            throw new ApiRequestException(Message.MESSAGE_081);
        }
        int overlapScheduleNumber = scheduleRepository
                .countOverlapSchedule(candidateId, scheduleFrom, scheduleTo);

        if (overlapScheduleNumber > 0) {
            throw new ApiRequestException(Message.MESSAGE_082);
        }

        Schedule schedule = convertToEntity(scheduleRequestDto);

        schedule.setScheduleStatus(ScheduleStatusEnum.WAITING_FOR_INTERVIEW);

        if (DateUtils.convertDateToString(scheduleFrom, DateFormat.DATE)
                .equals(DateUtils.convertDateToString(new Date(), DateFormat.DATE))) {
            schedule.setScheduleStatus(ScheduleStatusEnum.IN_PROGRESS);
        }

        schedule.setResult(ScheduleResultEnum.OPEN);
        try {
            scheduleRepository.save(schedule);
            return new MessageResponseDto(Message.MESSAGE_020, convertToDto(schedule), null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiRequestException(Message.MESSAGE_019);
        }

    }

    /**
     * This method edit interview-schedule by id
     *
     * @param scheduleId use get id schedule
     * @param requestDto use get information interview schedule
     * @return message
     */
    @Override
    public MessageResponseDto updateSchedule(Integer scheduleId, ScheduleRequestDto requestDto) {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        Date scheduleFrom = DateUtils.convertStringToDate(requestDto.getScheduleFrom());
        Date scheduleTo = DateUtils.convertStringToDate(requestDto.getScheduleTo());
        if (!schedule.isPresent()) {
            throw new ApiRequestException("ScheduleId: " + scheduleId + " is not exist");
        }
        if(scheduleFrom.after(scheduleTo)) {
            throw new ApiRequestException(Message.MESSAGE_081);
        }
        if(scheduleFrom.before(new Date())) {
            throw  new ApiRequestException(Message.MESSAGE_080);
        }
        if(scheduleFrom.equals(scheduleTo)) {
            throw new ApiRequestException("Schedule From doesn't equal scheduleTo");
        }
        //Status is Pass_interview or fail_interview then do not change status.
        ScheduleStatusEnum status = schedule.get().getScheduleStatus();
        if(status.equals(ScheduleStatusEnum.FAILED_INTERVIEW)||
            status.equals(ScheduleStatusEnum.PASSED_INTERVIEW)) {
            throw new ApiRequestException("Schedule status is pass or fail interview no allowed edit");
        }
        Schedule scheduleEntity = convertToEntity(requestDto);
        scheduleEntity.setResult(schedule.get().getResult());
        scheduleEntity.setCreatedDate(schedule.get().getCreatedDate());
        scheduleEntity.setCreatedBy(schedule.get().getCreatedBy());
        scheduleEntity.setScheduleId(schedule.get().getScheduleId());
        try {
            scheduleRepository.save(scheduleEntity);
            return new MessageResponseDto(Message.MESSAGE_022, "" , "");
        }catch (Exception ex) {
            throw new ApiRequestException(Message.MESSAGE_021);
        }
    }

    /**
     * This method use to set status of schedule start today to In-progress.
     * At the same time, this will set status of In-progress schedule that have time scheduleTo in the past to Cancelled interview.
     *
     */
    @Override
    @Transactional
    public void updateScheduleStatusEveryday() {
        scheduleRepository.updateInProgressSchedule(ScheduleStatusEnum.IN_PROGRESS);
        scheduleRepository.updateOverDateSchedule(ScheduleStatusEnum.CANCELLED_INTERVIEW);
    }



    /**
     * @return
     */
    @Override
    public DataCreateScheduleResponseDto getDataCreateSchedule() {
        List<Object> candidateList = candidateService.getCandidateListByStatus(CandidateStatusEnum.OPEN);
        List<Object> interviewerList = userService.getUserListByStatusAndRole(UserStatusEnum.ACTIVATED, RoleEnum.ROLE_INTERVIEWER);
        List<Object> recruiterList = userService.getUserListByStatusAndRole(UserStatusEnum.ACTIVATED, RoleEnum.ROLE_RECRUITER);

        return new DataCreateScheduleResponseDto(candidateList, interviewerList, recruiterList);
    }

    /**
     * This method use to get schedule list by status
     *
     * @param scheduleStatusEnum - is keyword to filter schedule list by status
     * @return list schedule
     */
    @Override
    public List<Object> getScheduleListByStatus(ScheduleStatusEnum scheduleStatusEnum) {
        return scheduleRepository.getScheduleListByStatus(scheduleStatusEnum.getKey());
    }

    /**
     * get all email of interview
     *
     * @return list email schedule
     */
    @Override
    public List<Schedule> getAllScheduleDateFrom() {
        LocalDate date = LocalDate.now();
        List<Schedule> scheduleList = new ArrayList<>();
        for (Schedule schedule : scheduleRepository.findAllEmailInterview(date)) {
            scheduleList.add(schedule);
        }
        return scheduleList;
    }

//TODO: Send mail remind interview schedule

    /**
     * Send email for interview
     *
     * @param listEmailInterview
     */
    @Override
    public void sendScheduleRemind(List<Schedule> listEmailInterview) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dayPlusAWeek = LocalDate.now();
        String day = formatter.format(dayPlusAWeek);
        if (listEmailInterview.isEmpty()) {
            throw new ApiNotFoundException(" Today: " + day + " no interviews");
        }
        try
        {
            for (Schedule schedule : listEmailInterview) {
               RemindScheduleResponseDto responseDto = convertToDtoSchedule(schedule);
               String filePath = FileUtils.getNewFileName(schedule.getCandidate().getCvAttachment(), schedule.getCandidate().getCandidateId());
                DataMailDto dataMailDto = new DataMailDto();
                dataMailDto.setTo(responseDto.getEmail());
                dataMailDto.setSubject("no-reply-email-IMS-system <Interview schedule title>");

                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("fullName", responseDto.getFullName());
                objectMap.put("scheduleId", responseDto.getScheduleId());
                objectMap.put("scheduleFrom",
                    DateUtils.convertDateToString(responseDto.getScheduleFrom(), "HH:mm a"));
                objectMap.put("scheduleTo",
                    DateUtils.convertDateToString(responseDto.getScheduleTo(), "HH:mm a"));
                objectMap.put("candidateName", responseDto.getFullName());
                objectMap.put("candidatePosition", responseDto.getOfferPosition());
                objectMap.put("recruiter", responseDto.getAccount());
                objectMap.put("meetingId", new StringBuffer().append("http://").append(responseDto.getMeetingId()));
                dataMailDto.setProps(objectMap);
                File dir = new File(pathValue);
                FilenameFilter filter = new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.startsWith(filePath);
                    }
                };
                String[] children = dir.list(filter);
                if (children.length == 0) {
                    emailService.sendMail(dataMailDto, "EMAIL_TEMPLATE_02");
                }
                else {
                    int dem = children.length;
                    for (int i=0; i < dem; i++) {
                        String filename = pathValue + children[i];
                        emailService.sendMail(dataMailDto, "EMAIL_TEMPLATE_02", filename);
                        System.out.println(filename);
                    }
                }
            }
        } catch (MessagingException e) {
            throw new ApiRequestException("Error sending email" + e);
        } catch (Exception e) {
            throw new ApiRequestException("An unexpected error occurred : " + e);
        }
    }

    @Override
    public RemindScheduleResponseDto convertToDtoSchedule(Schedule schedule) {
        return RemindScheduleResponseDto.builder()
            .scheduleId(schedule.getScheduleId())
            .email(schedule.getCandidate().getPerson().getEmail())
            .scheduleFrom(schedule.getScheduleFrom())
            .scheduleTo(schedule.getScheduleTo())
            .meetingId(schedule.getMeetingId())
            .cvAttachment(schedule.getCandidate().getCvAttachment())
            .fullName(schedule.getCandidate().getPerson().getFullName())
            .offerPosition(schedule.getCandidate().getPositionEnum().getValue())
            .account(schedule.getCandidate().getPerson().getUser().getAccount())
            .build();
    }
}
