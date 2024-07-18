package com.example.mockproject.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum UserStatusEnum {
    DE_ACTIVATED(0, "De-activated"),
    ACTIVATED(1, "Activated");

    private int key;
    private String value;

    public static UserStatusEnum getByKey(Integer key) {
        return Stream.of(UserStatusEnum.values())
                .filter(status -> status.key == key)
                .findFirst()
                .orElse(null);
    }
}
