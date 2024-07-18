package com.example.mockproject.service;

import com.example.mockproject.dto.request.PasswordDto;
import com.example.mockproject.dto.request.UserRequestDto;
import com.example.mockproject.dto.response.MessageResponseDto;
import com.example.mockproject.dto.response.UserResponseDto;
import com.example.mockproject.model.User;
import com.example.mockproject.utils.enums.RoleEnum;
import com.example.mockproject.utils.enums.UserStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public interface UserService {

    User findUserByAccount(String account);

    void resetPasswordToken(HttpServletRequest request,String email);

    User getByResetPasswordToken( String token) ;

    User checkPasswordUserExpiration(String password);

    void updatePassword(String token, PasswordDto passwordDto);

    String getSiteURL(HttpServletRequest request );

    void sendEmail(String recipientEmail, String link, String token) throws MessagingException, UnsupportedEncodingException;

    Page<UserResponseDto> searchUser (Pageable pageable, String name ,Integer role);

    MessageResponseDto createUser (UserRequestDto userRequestDto);

    String autoGenerateAccount (String fullName);

    Boolean checkUserHasRole(Integer userId, RoleEnum roleEnum);

    User getUserHasRole(Integer userId, RoleEnum roleEnum);

    List<Object> getUserListByStatusAndRole(UserStatusEnum userStatusEnum, RoleEnum roleEnum);

    void sendEmailRegister(String receiver, String account, String password);

}
