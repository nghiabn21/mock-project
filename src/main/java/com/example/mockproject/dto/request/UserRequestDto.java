package com.example.mockproject.dto.request;

import com.example.mockproject.utils.annotation.DateAnnotation;
import com.example.mockproject.utils.annotation.OrdinalEnumConstraint;
import com.example.mockproject.utils.annotation.ValueEnumConstraint;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.constant.Regex;
import com.example.mockproject.utils.enums.DepartmentEnum;
import com.example.mockproject.utils.enums.GenderEnum;
import com.example.mockproject.utils.enums.RoleEnum;
import com.example.mockproject.utils.enums.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = Message.MESSAGE_023)
    @Size(max = 255)
    @Pattern(regexp = Regex.FULL_NAME_PATTERN, message = Message.MESSAGE_064)
    private String fullName;

    @NotBlank(message = Message.MESSAGE_023)
    @Size(max = 255)
    @DateAnnotation
    private String dob;

    @NotBlank(message = Message.MESSAGE_023)
    @Size(max = 255)
    @Pattern(regexp = Regex.PHONE_PATTERN, message = Message.MESSAGE_059)
    private String phoneNumber;

    @NotBlank(message = Message.MESSAGE_023)
    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 255)
    private String address;

    @Size(max = 255)
    private String note;

    @NotNull(message = Message.MESSAGE_023)
    @OrdinalEnumConstraint(enumClass = GenderEnum.class,message = Message.MESSAGE_060)
    private String gender;

    @NotNull(message = Message.MESSAGE_023)
    @OrdinalEnumConstraint(enumClass = DepartmentEnum.class,message = Message.MESSAGE_061)
    private String department;

    @NotNull(message = Message.MESSAGE_023)
    private Set<@ValueEnumConstraint(enumClass = RoleEnum.class,
            message = Message.MESSAGE_062) String> roles;

    @NotNull(message = Message.MESSAGE_023)
    @OrdinalEnumConstraint(enumClass = UserStatusEnum.class, message = Message.MESSAGE_063)
    private String status;

}
