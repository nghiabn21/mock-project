package com.example.mockproject.controller;

import com.example.mockproject.dto.request.EmailDto;
import com.example.mockproject.dto.request.LoginDto;
import com.example.mockproject.dto.request.PasswordDto;
import com.example.mockproject.dto.request.UserRequestDto;
import com.example.mockproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * This method will send an email with a link to user click on it to change password
     * and temporary password if user do not want to change password
  //   * @param emailDto about email
     * @param request is a request
     * @return ResponseEntity
     */
    @PostMapping("/forgot_password")
    public ResponseEntity<?> processForgotPassword(@Valid @RequestBody EmailDto emailDto, HttpServletRequest request) {
        userService.resetPasswordToken(request, emailDto.getEmail());
        return ResponseEntity.ok("Send email successfully " );
    }

    /**
     * This method will respond a message when user login success with account
     * @param login
     * @return ResponseEntity
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto login) {
        return ResponseEntity.ok("Success with account: " + login.getAccount().trim() );
    }


    @GetMapping("/search-user")
    public ResponseEntity<?> searchUser(@RequestParam(defaultValue = "") String name,
                                        @RequestParam(required = false) Integer role,
                                        @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.searchUser(pageable, name, role));
    }

    /**
     * This api use to create new user account and send email to notify for user
     *
     * @param userRequestDto
     * @return ResponseEntity
     */
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser( @Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.createUser(userRequestDto));
    }
    /**
     * This method will change password's user
     * @param passwordDto about re-new password and new password
     * @param token is a string about 20 character
     * @return ResponseEntity
     */
    @PostMapping("/reset_password")
    public ResponseEntity<?> processResetPassword(@Valid @RequestBody PasswordDto passwordDto,
                                                  @RequestParam("token") String token) {
        userService.updatePassword(token, passwordDto);
        return ResponseEntity.ok("You have successfully changed your password.");
    }

}
