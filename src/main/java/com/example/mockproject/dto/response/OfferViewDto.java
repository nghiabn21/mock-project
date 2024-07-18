package com.example.mockproject.dto.response;

import com.example.mockproject.model.Offer;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferViewDto {
    private Integer offerId;
    private String candidateName;
    private String email;
    private String approver;
    private String department;
    private String notes;
    private String status;

    public static OfferViewDto convertToOfferViewDto(Offer offer) {
        if (offer == null) return null;

        return OfferViewDto.builder()
                .offerId(offer.getOfferId())
                .candidateName(offer.getSchedule().getCandidate().getPerson().getFullName())
                .status(offer.getOfferStatusEnum().getValue())
                .email(offer.getSchedule().getCandidate().getPerson().getEmail())
                .approver(offer.getManager().getPerson().getFullName())
                .department(offer.getDepartment().getValue())
                .notes(offer.getNote())
                .build();
    }
}
