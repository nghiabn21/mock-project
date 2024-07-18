package com.example.mockproject.dto.response;

import com.example.mockproject.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String account ;
    private String email;
    private String phoneNumber;
    private Set<Roles> roles;
    private String status;
}
