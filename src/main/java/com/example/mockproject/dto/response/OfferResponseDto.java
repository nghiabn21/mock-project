package com.example.mockproject.dto.response;

import com.example.mockproject.model.Offer;
import com.example.mockproject.model.User;
import com.example.mockproject.utils.common.DateUtils;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferResponseDto {

    private Integer offerId;

    private String candidateName;

    private String position;

    //approve by
    private String manager;

    private String status;

    private Set<String> interviews = new HashSet<>();

    private String contractStart;

    private String contractEnd;

    private String interviewNotes;

    private String contractType;

    private String level;

    private String department;

    private String recruiterOwner;

    private String dueDate;

    private BigDecimal basicSalary;

    private String note;
    //add field OfferStatus
    private String offerStatus;

    public static OfferResponseDto toDto(Offer offer) {
        if (offer == null) return null;

        return OfferResponseDto.builder()
                .offerId(offer.getOfferId())
                .candidateName(offer.getSchedule().getCandidate().getPerson().getFullName())
                .position(offer.getPosition().getValue())
                .manager(offer.getManager().getAccount())
                .offerStatus(offer.getOfferStatusEnum().getValue())
                .status(offer.getSchedule().getCandidate().getCandidateStatus().getValue())
                .interviews(
                        offer
                                .getSchedule()
                                .getInterviewers()
                                .stream()
                                .map(User::getAccount)
                                .collect(Collectors.toSet())
                )
                .contractStart(DateUtils.convertDateToString(offer.getContractStart()))
                .contractEnd(DateUtils.convertDateToString(offer.getContractEnd()))
                .interviewNotes(offer.getSchedule().getNotes())
                .contractType(offer.getContractType().getValue())
                .level(offer.getLevel().getValue())
                .department(offer.getDepartment().getValue())
                .recruiterOwner(offer.getRecruiterOwner().getAccount())
                .dueDate(DateUtils.convertDateToString(offer.getDueDate()))
                .basicSalary(offer.getBasicSalary() == 0 ? new BigDecimal(0) : new BigDecimal(offer.getBasicSalary()))
                .note(offer.getNote())
                .build();
    }

}
