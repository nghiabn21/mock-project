package com.example.mockproject.service.impl;

import com.example.mockproject.dto.request.OfferRequestDto;
import com.example.mockproject.dto.request.OfferStatusRequestDto;
import com.example.mockproject.dto.response.*;
import com.example.mockproject.model.Offer;
import com.example.mockproject.model.Schedule;
import com.example.mockproject.model.User;
import com.example.mockproject.repository.OfferRepository;
import com.example.mockproject.repository.ScheduleRepository;
import com.example.mockproject.repository.UserRepository;
import com.example.mockproject.service.EmailService;
import com.example.mockproject.service.OfferService;
import com.example.mockproject.service.ScheduleService;
import com.example.mockproject.service.UserService;
import com.example.mockproject.utils.common.DateUtils;
import com.example.mockproject.utils.common.FileUtils;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.enums.*;
import com.example.mockproject.utils.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FilenameFilter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserService userService;
    private final ScheduleService scheduleService;
    private final String pathValue ="D:\\";


    /**
     * @param id - offer id want to search
     * @return response offer after edit
     */
    @Override
    public OfferResponseDto findByID(Integer id) {
        try {
            Offer offer = this.offerRepository.findById(id).orElseThrow(
                    () -> new ApiRequestException("There doesn't exist an offer with that id: " + id)
            );
            return OfferResponseDto.toDto(offer);
        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("ID offer is not null!");
        }
    }

    /**
     * This method use to edit Offer
     * @param id         - offer id want to edit
     * @param requestDto - offer information used for editing
     * @return response offer after edit
     */
    @Override
    public OfferResponseDto update(Integer id, OfferRequestDto requestDto) {
        PositionEnum position;
        try {
            position = Optional.of(PositionEnum.getByKey(Integer.parseInt(requestDto.getPosition()))).get();
        } catch (NumberFormatException | NullPointerException e) {
            throw new ApiRequestException("Position is not exist!");
        }
        ContractTypeEnum contractType;
        try {
            contractType = Optional.of(ContractTypeEnum.getByKey(Integer.parseInt(requestDto.getContractType()))).get();
        } catch (NumberFormatException | NullPointerException e) {
            throw new ApiRequestException("ContractType is not exist!");
        }
        LevelEnum level;
        try {
            level = Optional.of(LevelEnum.getByKey(Integer.parseInt(requestDto.getLevel()))).get();
        } catch (NumberFormatException | NullPointerException e) {
            throw new ApiRequestException("Level is not exist!");
        }
        DepartmentEnum department;
        try {
            department = Optional.of(DepartmentEnum.getByKey(Integer.parseInt(requestDto.getDepartment()))).get();
        } catch (NumberFormatException | NullPointerException e) {
            throw new ApiRequestException("Department is not exist!");
        }
        OfferStatusEnum status;
        try {
            status = Optional.of(OfferStatusEnum.getByKey(Integer.parseInt(requestDto.getStatus()))).get();
        } catch (NumberFormatException | NullPointerException e) {
            throw new ApiRequestException("Department is not exist!");
        }
        Date dueDate = DateUtils.convertStringToDate(requestDto.getDueDate());
        Date contractStart = DateUtils.convertStringToDate(requestDto.getContractStart());
        Date contractEnd = DateUtils.convertStringToDate(requestDto.getContractEnd());
        Date today = Calendar.getInstance().getTime();

        Offer offer = this.offerRepository.findById(id).orElseThrow(
                () -> new ApiRequestException("There doesn't exist an offer with that id: " + id)
        );

        if (!dueDate.equals(offer.getDueDate()) && today.after(dueDate))
            throw new ApiRequestException("Due date must be greater than current date !");

        if (!contractStart.equals(offer.getContractStart()) && dueDate.after(contractStart))
            throw new ApiRequestException("Contract start date must be greater than due date !");

        if (!contractEnd.equals(offer.getContractEnd()) && contractStart.after(contractEnd))
            throw new ApiRequestException("Contract end date must be greater than contract start !");

        offer.setSchedule(
                this.scheduleRepository.findById(Integer.parseInt(requestDto.getScheduleId()))
                        .orElseThrow(() -> new ApiRequestException("There doesn't exist an Schedule with that id: " + requestDto.getScheduleId()))
        );

        User manager = this.userRepository.findById(Integer.parseInt(requestDto.getManagerId()))
                .orElseThrow(() -> new ApiRequestException("There doesn't exist an Manager with that id: " + requestDto.getManagerId()));
        if (manager.getRoles()
                .stream()
                .noneMatch(roles -> roles.getRole().equals(RoleEnum.ROLE_MANAGER))
        ) throw new ApiRequestException("Invalid role manager!");
        offer.setManager(manager);

        User recruiterOwner = this.userRepository.findById(Integer.parseInt(requestDto.getRecruiterOwnerId()))
                .orElseThrow(() -> new ApiRequestException("There doesn't exist an Recruiter Owner with that id: " + requestDto.getRecruiterOwnerId()));
        if (recruiterOwner.getRoles()
                .stream()
                .noneMatch(roles -> roles.getRole().equals(RoleEnum.ROLE_RECRUITER))
        ) throw new ApiRequestException("Invalid role recruiter owner!");
        offer.setRecruiterOwner(recruiterOwner);

        offer.setPosition(position);
        offer.setContractType(contractType);
        offer.setLevel(level);
        offer.setDepartment(department);
        offer.setContractStart(contractStart);
        offer.setContractEnd(contractEnd);
        offer.setDueDate(dueDate);
        offer.setOfferStatusEnum(status);

        if (!"".equals(requestDto.getBasicSalary()) && requestDto.getBasicSalary() != null) {
            offer.setBasicSalary(Double.parseDouble(requestDto.getBasicSalary()));
        }
        offer.setNote(requestDto.getNote());

        return OfferResponseDto.toDto(this.offerRepository.saveAndFlush(offer));
    }

    /**
     * This method use to get page Offer and filter by candidateName, department and status
     *
     * @param name       - is keyword to search Offer by candidateName
     * @param department - is id of departmentName use to filter page Offer
     * @param status     - is id of candidateStatus use to filter page Offer
     * @param pageable   - use to get page number and page size
     * @return Page<Offer>
     */
    @Override
    public Page<OfferViewDto> getOfferPage(String name, Integer department, Integer status, Pageable pageable) {
        if (pageable.getPageNumber()*pageable.getPageSize() < 0) {
            throw new ApiRequestException("Invalid request");
        }
        if (pageable.getPageSize()>50) {
            pageable = PageRequest.of(pageable.getPageNumber(), 50);
        }else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        }
        Page<Offer> offerPage = offerRepository.findAllByCandidateNameAndDepartmentAndStatus(name, department, status, pageable);
        Page<OfferViewDto> viewDtos = offerPage.map(OfferViewDto::convertToOfferViewDto);
        return viewDtos;
    }

    /**
     * This method is used to get offer number need to approval by managerId
     * @return - offer number need to approval
     */
    @Override
    public Integer getCurrentOffer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        int idM = userRepository.findUserByAccountIgnoreCase(name).getUserId();
        return offerRepository.countCurOfferByManagerIdAndOfferStatus(idM);
    }

    @Override
    public DataCreateOfferResponseDto getDataCreateOffer() {
        List<Object> scheduleList = scheduleService.getScheduleListByStatus(ScheduleStatusEnum.PASSED_INTERVIEW);
        List<Object> managerList = userService.getUserListByStatusAndRole(UserStatusEnum.ACTIVATED, RoleEnum.ROLE_MANAGER);
        List<Object> recruiterList = userService.getUserListByStatusAndRole(UserStatusEnum.ACTIVATED, RoleEnum.ROLE_RECRUITER);

        return new DataCreateOfferResponseDto(scheduleList, managerList, recruiterList);
    }

    /**
     * This method use to create a new Offer
     *
     * @param offerRequestDto - is requestBody use to get information of new offer
     * @return MessageResponseDto for response message and offer after created
     */
    @Override
    public MessageResponseDto addNewOffer(OfferRequestDto offerRequestDto) {
        Date contractStart = DateUtils.convertStringToDate(offerRequestDto.getContractStart());
        Date contractEnd = DateUtils.convertStringToDate(offerRequestDto.getContractEnd());
        Date dueDate = DateUtils.convertStringToDate(offerRequestDto.getDueDate());

        if (contractStart.before(new Date())) {
            throw new ApiRequestException("contractStart must be in the future");
        }
        if (contractStart.after(contractEnd)) {
            throw new ApiRequestException("contractStart must be before contractEnd");
        }
        if (dueDate.before(new Date())) {
            throw new ApiRequestException("dueDate must be in the future");
        }

        Optional<Schedule> schedule = scheduleRepository.findByScheduleIdAndScheduleStatus(Integer.parseInt(offerRequestDto.getScheduleId()), ScheduleStatusEnum.PASSED_INTERVIEW);
        User manager = userService.getUserHasRole(Integer.parseInt(offerRequestDto.getManagerId()), RoleEnum.ROLE_MANAGER);
        User recruiter = userService.getUserHasRole(Integer.parseInt(offerRequestDto.getRecruiterOwnerId()), RoleEnum.ROLE_RECRUITER);
        Optional<Offer> offerOptional = offerRepository.findByScheduleId(Integer.parseInt(offerRequestDto.getScheduleId()));

        if (schedule.isEmpty()) {
            throw new ApiRequestException("Schedule is not found with status pass");
        }
        if (offerOptional.isPresent()) {
            throw new ApiRequestException("Schedule is used");
        }

        Offer offer = OfferRequestDto.toEntity(offerRequestDto);
        offer.setManager(manager);
        offer.setRecruiterOwner(recruiter);
        offer.setSchedule(schedule.get());
        offer.setOfferStatusEnum(OfferStatusEnum.WAITING_FOR_APPROVAL);
        try {
            offerRepository.save(offer);
            return new MessageResponseDto(Message.MESSAGE_025, OfferResponseDto.toDto(offer), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageResponseDto(Message.MESSAGE_024, null, null);
        }
    }

    /**
     * Using approved/reject offer
     *
     * @param id                    - id of offer
     * @param offerStatusRequestDto - contains fields to update
     * @return OfferStatusResponseDto contain OfferStatus(approved/reject), notes
     */
    @Override
    public OfferResponseDto changeStatusOffer(int id, OfferStatusRequestDto offerStatusRequestDto) {

        Offer offer = offerRepository.findByOfferId(id)
                .orElseThrow(() -> new ApiRequestException("Offer not found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        int idM = userRepository.findUserByAccountIgnoreCase(name).getUserId();
        int idN = offer.getManager().getUserId();
        if (!(idM == idN)){
            throw new ApiRequestException("Manager cannot approval/reject offer");
        }
        if (offer.getOfferStatusEnum().equals(OfferStatusEnum.APPROVED_OFFER)) {
            throw new ApiRequestException("Approved status does not allow editing");
        }
        if (offer.getOfferStatusEnum().equals(OfferStatusEnum.REJECTED_OFFER)) {
            throw new ApiRequestException("Reject status does not allow editing");
        }
        offer.setOfferStatusEnum(OfferStatusEnum.getByKey(Integer.parseInt(offerStatusRequestDto.getOfferStatus())));
        offer.setNote(offerStatusRequestDto.getNote());
        return OfferResponseDto.toDto(offerRepository.save(offer));
    }

    /**
     * Using converter Offer to OfferRemindResponseDto
     *
     * @param offer
     * @return OfferRemindResponseDto
     */
    public OfferRemindResponseDto convertToDto(Offer offer) {
        OfferRemindResponseDto offerRMDto = new OfferRemindResponseDto();
        offerRMDto.setOfferId(offer.getOfferId());
        offerRMDto.setCandidateName(offer.getSchedule().getCandidate().getPerson().getFullName());
        offerRMDto.setCandidatePosition(offer.getPosition().getValue());
        offerRMDto.setOfferDueDate(offer.getDueDate());
        offerRMDto.setOfferRecruiterOwnerAccount(offer.getRecruiterOwner().getAccount());
        offerRMDto.setEmailApprovedBy(offer.getManager().getPerson().getEmail());
        return offerRMDto;
    }

    /**
     * Using get the list of offers due to dueDate
     *
     * @return list offer
     */
    @Override
    public List<Offer> getAllOfferDueDate() {
        LocalDate dateDue = LocalDate.now();
        List<Offer> listOffer = new ArrayList<>();
        for (Offer offer : offerRepository.findAllByDueDate(dateDue)) {
            listOffer.add(offer);
        }
        return listOffer;
    }

    /**
     * Used to send email reminders about offers that need approval
     *
     * @param offerList list offer due date to handle
     * @return message notifying email delivery success or exception
     */
    @Override
    public void sendEmailReminder(List<Offer> offerList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dayPlusAWeek = LocalDate.now();
        String day = formatter.format(dayPlusAWeek);
        if (offerList.isEmpty()) {
            throw new ApiRequestException("Today"+" "+ day+" "+"No offer has been reached");
        }
        try {
            for (Offer offer : offerList) {
                OfferRemindResponseDto offerRM = convertToDto(offer);

                String filePath = FileUtils.getNewFileName(
                        offer.getSchedule().getCandidate().getCvAttachment(),
                        offer.getSchedule().getCandidate().getCandidateId());

                DataMailDto dataMail = new DataMailDto();
                dataMail.setTo(offerRM.getEmailApprovedBy());
                dataMail.setSubject("no-reply-email-IMS-system <Take action on Job offer>");

                Map<String, Object> props = new HashMap<>();
                props.put("candidateName", offerRM.getCandidateName());
                props.put("position", offerRM.getCandidatePosition());
                props.put("dueDate", offerRM.getOfferDueDate());
                props.put("id", offerRM.getOfferId());
                props.put("recruiterOwnerAccount", offerRM.getOfferRecruiterOwnerAccount());
                props.put("attachment", filePath);

                dataMail.setProps(props);
                String file = "D:\\FileCv\\" + filePath + ".pdf";
                dataMail.setProps(props);
                FileSystemResource files
                        = new FileSystemResource(
                        new File(file));
                if (files.exists()) {
                    dataMail.setAttachment(filePath);
                    emailService.sendMail(dataMail, "EMAIL_TEMPLATE_REMINDER",file);
                }else {
                    dataMail.setAttachment("<No file>");

                    emailService.sendMail(dataMail, "EMAIL_TEMPLATE_REMINDER");
                }
            }
        } catch (MessagingException exp) {
            throw new ApiRequestException("Error sending email" + exp);
        }
        throw new ApiRequestException("Send email reminder successful");

    }
}
