package com.example.mockproject.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ContractTypeEnum {
    TRIAL_TWO_MONTHS(0, "Trial 2 months"),
    TRAINEE_THREE_MONTHS(1, "Trainee 3 months"),
    ONE_YEAR(2, "1 year"),
    THREE_YEARS(3, "3 years"),
    UNLIMITED(4, "Unlimited");

    private int key;
    private String value;

    public static ContractTypeEnum getByKey(Integer key) {
        return Stream.of(ContractTypeEnum.values())
                .filter(status -> status.key == key)
                .findFirst()
                .orElse(null);
    }
}
