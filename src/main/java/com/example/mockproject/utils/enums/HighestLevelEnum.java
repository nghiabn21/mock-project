package com.example.mockproject.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum HighestLevelEnum {
    HIGHEST_SCHOOL(0, "High school"),
    BACHELOR_DEGREE(1, "Bachelor’s Degree"),
    MASTER_DEGREE(2, "Master Degree, PhD");

    private int key;
    private String value;

    public static HighestLevelEnum getByKey(Integer key) {
        return Stream.of(HighestLevelEnum.values())
                .filter(status -> status.key == key)
                .findFirst()
                .orElse(null);
    }
}
