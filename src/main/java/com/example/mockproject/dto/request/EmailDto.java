package com.example.mockproject.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

    @NotNull(message = "Email is must required")
    @Email(message = "Email is not valid")
    private String email;
}
