package com.example.mockproject.dto.request;

import com.example.mockproject.utils.constant.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExportRequestDto {
    @NotBlank(message = Message.MESSAGE_023)
    private String dateFrom;

    @NotBlank(message = Message.MESSAGE_023)
    private String dateTo;
}
