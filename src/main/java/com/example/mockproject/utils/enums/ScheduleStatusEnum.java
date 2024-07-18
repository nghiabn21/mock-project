package com.example.mockproject.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ScheduleStatusEnum {
    OPEN(0, "Open"),
    WAITING_FOR_INTERVIEW(1, "Waiting for interview"),
    IN_PROGRESS(2, "In-progress"),
    CANCELLED_INTERVIEW(3, "Cancelled interview"),
    PASSED_INTERVIEW(4, "Passed Interview"),
    FAILED_INTERVIEW(5, "Failed interview");


    private int key;
    private String value;

    public static ScheduleStatusEnum getByKey(Integer key) {
        for (ScheduleStatusEnum status : ScheduleStatusEnum.values()) {
            if (status.getKey() == key) {
                return status;
            }
        }
        return null;
    }
}
