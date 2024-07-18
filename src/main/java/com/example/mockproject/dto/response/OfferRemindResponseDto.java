package com.example.mockproject.dto.response;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferRemindResponseDto {
    private Integer offerId;
    private String candidateName;
    private String candidatePosition;
    private Date offerDueDate;
    private String offerRecruiterOwnerAccount;
    private String emailApprovedBy;
}
