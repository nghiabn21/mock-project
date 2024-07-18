package com.example.mockproject.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum LevelEnum {
    FRESHER_1(0, "Fresher 1"),
    JUNIOR_2_1(1, "Junior 2.1"),
    JUNIOR_2_2(2, "Junior 2.2"),
    SENIOR_3_1(3, "Senior 3.1"),
    SENIOR_3_2(4, "Senior 3.2"),
    DELIVERY(5, "Delivery"),
    LEADER(6, "Leader"),
    MANAGER(7, "Manager"),
    VICE_HEAD(8, "Vice Head");

    private int key;
    private String value;

    public static LevelEnum getByKey(Integer key) {
        return Stream.of(LevelEnum.values())
                .filter(status -> status.key == key)
                .findFirst()
                .orElse(null);
    }
}
