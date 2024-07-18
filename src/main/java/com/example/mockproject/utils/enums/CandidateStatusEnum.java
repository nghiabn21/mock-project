package com.example.mockproject.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum CandidateStatusEnum {
    OPEN(0, "Open"),
    BANNED(1, "Banned");

    private int key;
    private String value;

    public static CandidateStatusEnum getByKey(Integer key) {
        for (CandidateStatusEnum status : CandidateStatusEnum.values()) {
            if (status.getKey() == key) {
                return status;
            }
        }
        return null;
    }
}
