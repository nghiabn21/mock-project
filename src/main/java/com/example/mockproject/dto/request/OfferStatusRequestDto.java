package com.example.mockproject.dto.request;

import com.example.mockproject.utils.annotation.ValueEnumConstraintCustomize;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.enums.OfferStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferStatusRequestDto {

    @Length(max = 255, message = "Must less than or equal 256 characters")
    private String note;

    @NotBlank(message = Message.MESSAGE_023)
    @ValueEnumConstraintCustomize(enumClass = OfferStatusEnum.class, message = "offer_status must be in [0, 1, 2]")
    private String offerStatus;

}
