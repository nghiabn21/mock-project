package com.example.mockproject.dto.request;

import com.example.mockproject.utils.annotation.PasswordValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {

    @NotNull(message = "New Password is must required")
    @PasswordValidation
    private String newPassword;

    @NotNull(message = "Re-enter Password is must required")
    @PasswordValidation
    private String reNewPassword;
}