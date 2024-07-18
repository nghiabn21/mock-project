package com.example.mockproject.utils.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum GenderEnum {
    FEMALE(0, "Female"),
    MALE(1, "Male");

    private int key;
    private String value;

    public static GenderEnum getByKey(Integer key) {
        return Stream.of(GenderEnum.values())
                .filter(status -> status.key == key)
                .findFirst()
                .orElse(null);
    }
}
