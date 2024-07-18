package com.example.mockproject.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum RoleEnum {
    ROLE_ADMIN(1, "ROLE_ADMIN"),
    ROLE_RECRUITER(2, "ROLE_RECRUITER"),
    ROLE_MANAGER(3, "ROLE_MANAGER"),
    ROLE_INTERVIEWER(4, "ROLE_INTERVIEWER");

    private int key;
    private String value;

    public static RoleEnum getByKey(Integer key) {
        return Stream.of(RoleEnum.values())
                .filter(status -> status.key == key)
                .findFirst()
                .orElse(null);
    }

}
