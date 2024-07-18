package com.example.mockproject.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum PositionEnum {
    BACKEND_DEVELOPER(0, "Backend Developer"),
    BUSINESS_ANALYST(1, "Business Analyst"),
    TESTER(2, "Tester"),
    HR(3, "HR"),
    PROJECT_MANAGER(4, "Project manager");

    private int key;
    private String value;

    public static PositionEnum getByKey(Integer key) {
        return Stream.of(PositionEnum.values())
                .filter(status -> status.key == key)
                .findFirst()
                .orElse(null);
    }
}
