package com.example.mockproject.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum OfferStatusEnum {
    WAITING_FOR_APPROVAL(0, "Waiting for approval"),
    APPROVED_OFFER(1, "Approved offer"),
    REJECTED_OFFER(2, "Rejected offer"),
    WAITING_FOR_RESPONSE(3, "Waiting for response"),
    ACCEPTED_OFFER(4, "Accepted offer"),
    DECLINED_OFFER(5, "Declined offer"),
    CANCELLED_OFFER(6, "Cancelled offer");

    private int key;
    private String value;

    public static OfferStatusEnum getByKey(Integer key) {
        for (OfferStatusEnum status : OfferStatusEnum.values()) {
            if (status.getKey() == key) {
                return status;
            }
        }
        return null;
    }
}
