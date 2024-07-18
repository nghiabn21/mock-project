package com.example.mockproject.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ScheduleResultEnum {
    OPEN(0, "Open"),
    FAIL(1, "Fail"),
    PASS(2, "Pass"),
    CANCEL(3, "Cancel");

    private int key;
    private String value;

    public static ScheduleResultEnum getByKey(Integer key) {
        for (ScheduleResultEnum status : ScheduleResultEnum.values()) {
            if (status.getKey() == key) {
                return status;
            }
        }
        return null;
    }
}
