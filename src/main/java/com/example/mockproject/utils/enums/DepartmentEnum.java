package com.example.mockproject.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum DepartmentEnum {
    IT(0, "IT"),
    HR(1, "HR"),
    FINANCE(2, "Finance"),
    COMMUNICATION(3, "Communication"),
    MARKETING(4, "Marketing"),
    ACCOUNTING(5, "Accounting");

    private int key;
    private String value;

    public static DepartmentEnum getByKey(Integer key) {
        return Stream.of(DepartmentEnum.values())
                .filter(status -> status.key == key)
                .findFirst()
                .orElse(null);
    }
}
