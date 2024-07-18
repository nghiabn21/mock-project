package com.example.mockproject.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum JobStatusEnum {
    OPEN(0, "Open"),
    IN_PROGRESS(1, "In-progress"),
    CLOSED(2, "Closed");

    private int key;
    private String value;

    public static JobStatusEnum getByKey(Integer key) {
        return Stream.of(JobStatusEnum.values())
                .filter(status -> status.key == key)
                .findFirst()
                .orElse(null);
    }

    public static JobStatusEnum getByValue(String value) {
        return Stream.of(JobStatusEnum.values())
                .filter(status -> status.value.equals(value))
                .findFirst()
                .orElse(null);
    }
}
