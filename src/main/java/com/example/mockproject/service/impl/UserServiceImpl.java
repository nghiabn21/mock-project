package com.example.mockproject.service.impl;

import com.example.mockproject.dto.request.PasswordDto;
import com.example.mockproject.dto.request.UserRequestDto;
import com.example.mockproject.dto.response.DataMailDto;
import com.example.mockproject.dto.response.MessageResponseDto;
import com.example.mockproject.dto.response.UserResponseDto;
import com.example.mockproject.model.Person;
import com.example.mockproject.model.Roles;
import com.example.mockproject.model.User;
import com.example.mockproject.repository.PersonRepository;
import com.example.mockproject.repository.RoleRepository;
import com.example.mockproject.repository.UserRepository;
import com.example.mockproject.service.EmailService;
import com.example.mockproject.service.UserService;
import com.example.mockproject.utils.common.DateUtils;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.enums.DepartmentEnum;
import com.example.mockproject.utils.enums.GenderEnum;
import com.example.mockproject.utils.enums.RoleEnum;
import com.example.mockproject.utils.enums.UserStatusEnum;
import com.example.mockproject.utils.exception.ApiNotFoundException;
import com.example.mockproject.utils.exception.ApiRequestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;


@Transactional
@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private static final int EXPIRATION = 5 * 1;
    private final UserRepository userRepo;
    private SpringTemplateEngine templateEngine;

    private JavaMailSender mailSender;

    private final UserRepository userRepository;

    private final PersonRepository personRepository;

    private final RoleRepository roleRepository;

    private final EmailService emailService;


    /**
     * This method will find user by account
     *
     * @param account from request
     * @return User
     */
    @Override
    public User findUserByAccount(String account) {
        User user = userRepository.findUserByAccountIgnoreCase(account);
        return user;
    }

    /**
     * This method will update the user's information with the token, temporary password(encrypted) and its lifetime
     * Then will create a link and token to pass an email
     *
     * @param request is a request
     * @param email   is used to find user's email
     */
    @Override
    public void resetPasswordToken(HttpServletRequest request, String email) {
        String token = RandomString.make(20);
        String temporaryPassword = RandomString.make(8);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userRepo.findUserByPersonEmail(email);
        if (user != null) {
            List<String> list = user.getRoles().stream().map(role -> role.getRole().getValue()).collect(Collectors.toList());
            if ((list.contains("ROLE_RECRUITER") || list.contains("ROLE_MANAGER") || list.contains("ROLE_INTERVIEWER"))
                    && !list.contains("ROLE_ADMIN")) {
                user.setResetPasswordToken(token);
                user.setExpiryDate(calculateExpiryDate(EXPIRATION));
                user.setTemporaryPassword(passwordEncoder.encode(temporaryPassword));
                userRepo.save(user);
            } else {
                throw new ApiRequestException("This User " + user.getAccount() + " not authorization to send email");
            }
        } else {
            throw new ApiNotFoundException("Could not find any User with the email " + email);
        }
        String resetPasswordLink = getSiteURL(request) + "/reset_password?token=" + token;
        sendEmail(email, resetPasswordLink, temporaryPassword);
    }

    /**
     * This method will take the current time and add 10 minutes
     *
     * @param expiryTimeInMinutes 5 minutes
     * @return Date
     */
    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    /**
     * This method will check if the user's token exists or not
     * If so, continue to check if the user's temporary password is still valid.
     *
     * @param token is a string has been encode
     * @return User
     */
    @Override
    public User getByResetPasswordToken(String token) {
        User user = userRepo.findByResetPasswordToken(token);
        return user == null ? null : isTokenExpired(user) ? null : user;
    }

    /**
     * This method will check if the user's account exists or not
     * If so, continue to check if the user's temporary password is still valid.
     *
     * @param account use to check user has existed or not
     * @return User
     */
    @Override
    public User checkPasswordUserExpiration(String account) {
        User user = userRepo.findUserByAccountIgnoreCase(account);
        return user == null ? null : isTokenExpired(user) ? null : user;
    }

    /**
     * This method will compare the date the password was generated with the current time
     * If password creation time is before current time, it will return false else return true
     *
     * @param user use to get expiryDate
     * @return boolean
     */
    private boolean isTokenExpired(User user) {
        Calendar calendar = Calendar.getInstance();
        return user.getExpiryDate().before(calendar.getTime());
    }

    /**
     * This method will update the user information once the password has been changed successfully
     *
     * @param token       is a string has been encode
     * @param passwordDto about re-new password and new password
     */
    @Override
    public void updatePassword(String token, PasswordDto passwordDto) {
        if (!passwordDto.getReNewPassword().equals(passwordDto.getNewPassword())) {
            throw new ApiRequestException("Re-Enter Password is not equal new Password");
        }
        User user = getByResetPasswordToken(token);
        if (user == null) {
            throw new ApiNotFoundException("Could not find any User with the token " + token);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setTemporaryPassword(null);
        user.setExpiryDate(null);

        userRepo.save(user);

    }

    /**
     * This method will edit URL to  http://localhost:8080
     *
     * @param request is a request
     * @return String
     */
    @Override
    public String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        log.info(siteURL.replace(request.getServletPath(), ""));
        return siteURL.replace(request.getServletPath(), "");
    }

    /**
     * This method will create an email form with token and a link
     *
     * @param recipientEmail on behalf of the recipient
     * @param link           is a link for the user to click on it
     * @param token          is a string about 8 character
     */
    @Override
    public void sendEmail(String recipientEmail, String link, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Map<String, Object> model = new HashMap<String, Object>();
            Context context = new Context();
            model.put("link", link);
            model.put("token", token);
            String subject = "Here's the link to reset your password";
            helper.setFrom("NghiaHV5@fsoft.com.vn", "Security Support");
            helper.setTo(recipientEmail);
            context.setVariables(model);
            String html = templateEngine.process("SendMail", context);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (ApiRequestException | MessagingException | UnsupportedEncodingException ex) {
            throw new ApiRequestException("Error: " + ex.getMessage());
        }
    }

    /**
     * Using search user with properties(name , email , role)
     *
     * @param pageable
     * @param name     (User has Approved By)
     * @return page contains field UserResponseDto
     */
    @Override
    public Page<UserResponseDto> searchUser(Pageable pageable, String name, Integer role) {
        if (pageable.getPageNumber()*pageable.getPageSize() < 0) {
            throw new ApiRequestException("Invalid request");
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()
                , Sort.by("user_id").ascending());
        Page<User> pageOfAcc = userRepository.findAllByAccountAndRoles(pageable, name, role);
        Page<UserResponseDto> pageOfUserDTO = pageOfAcc.map(empE -> convertToDto(empE));
        return pageOfUserDTO;
    }

    /**
     * This method use to create new user account and send email to notify for user
     *
     * @param userRequestDto
     * @return MessageResponseDto
     */
    @Transactional
    @Override
    public MessageResponseDto createUser(UserRequestDto userRequestDto) {
        if (DateUtils.checkAge(userRequestDto.getDob()) == false) {
            throw new ApiRequestException("Age is invalid ! ");
        }
        if (personRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new ApiRequestException("Email " + userRequestDto.getEmail() + " is existed !");
        }
        if ((userRequestDto.getPhoneNumber() != null) && !userRequestDto.getPhoneNumber().trim().isEmpty()) {
            if (personRepository.existsByPhoneNumber(userRequestDto.getPhoneNumber())) {
                throw new ApiRequestException(Message.MESSAGE_030);
            }
        }
        User user = convertToEntity(userRequestDto);
        String passWordStr = RandomString.make(8);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(passWordStr));

        String accountStr = autoGenerateAccount(userRequestDto.getFullName());
        if (userRepository.findByAccount(accountStr).isPresent()) {
            throw new ApiRequestException("Account is existed !");
        }
        user.setAccount(accountStr);
        sendEmailRegister(userRequestDto.getEmail(), accountStr, passWordStr);
        try {
            userRepository.save(user);
            return new MessageResponseDto(Message.MESSAGE_057, null, null);
        } catch (Exception e) {
            return new MessageResponseDto(Message.MESSAGE_058, null, null);
        }
    }

    /**
     * This method use to convert UserRequestDto to User entity
     *
     * @param userRequestDto
     * @return User entity
     */
    public User convertToEntity(UserRequestDto userRequestDto) {
        User user = new User();
        Person person = new Person();
        person.setFullName(userRequestDto.getFullName());
        person.setDob(DateUtils.convertStringToDate(userRequestDto.getDob()));
        person.setPhoneNumber(userRequestDto.getPhoneNumber());
        person.setEmail(userRequestDto.getEmail());
        person.setAddress(userRequestDto.getAddress());
        person.setGender(GenderEnum.getByKey(Integer.parseInt(userRequestDto.getGender())));
        user.setPerson(person);
        user.setNote(userRequestDto.getNote());
        user.setStatus(UserStatusEnum.ACTIVATED);
        user.setDepartment(DepartmentEnum.getByKey(Integer.parseInt(userRequestDto.getDepartment())));
        Set<String> setRoleStr = new HashSet<>(userRequestDto.getRoles());
        Set<Roles> setRoles = new HashSet<>();
        for (String roleStr : setRoleStr) {
            setRoles.add(roleRepository.findByName(roleStr)
                    .orElseThrow(() -> new ApiRequestException("Role dose not exist")));
        }
        user.setRoles(setRoles);
        return user;
    }

    /**
     * This method use to create account from full name
     *
     * @param fullName
     * @return account
     */
    @Override
    public String autoGenerateAccount(String fullName) {
        String[] arrayName = fullName.split("\\s+");
        int lengthOfArrayName = arrayName.length;
        StringBuilder firstName = new StringBuilder(arrayName[lengthOfArrayName - 1]);
        for (int i = 0; i < (lengthOfArrayName - 1); i++) {
            String firstLetter = arrayName[i].substring(0, 1);
            firstName.append(firstLetter);
        }
        String account = stripAccents(firstName.toString());
        List<String> usernameList = this.userRepository.findByUsernameEndWith(account);
        if (usernameList.isEmpty()) {
            return account;
        }
        int maxIndex = usernameList.parallelStream()
                .map(s -> s.replaceAll(account, ""))
                .filter(s -> s.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        return account + ++maxIndex;
    }

    /**
     * Remove accents from a Unicode string
     *
     * @param text - string want to process
     * @return string has been processed
     */
    private String stripAccents(String text) {
        text = text.replaceAll("Đ", "D").replaceAll("đ", "d");
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}", "");
    }

    /**
     * @param receiver
     * @param account
     * @param password
     * @return MessageResponseDto
     */
    @Override
    public void sendEmailRegister(String receiver, String account, String password) {
        DataMailDto dataMailDto = new DataMailDto();
        dataMailDto.setTo(receiver);
        dataMailDto.setSubject("no-reply-email-IMS-system " + account);
        Map<String, Object> props = new HashMap<>();
        props.put("account", account);
        props.put("password", password);
        dataMailDto.setProps(props);
        try {
            emailService.sendMail(dataMailDto, "EMAIL_TEMPLATE_03");
            log.info("Email sent successfully !");
        } catch (MessagingException e) {
            log.error("Send Email Register fail !");
            e.getMessage();
        }
    }

    /**
     * This method use to check user has a specific role
     *
     * @param userId   is id of user that will be get from DB
     * @param roleEnum is enum use to check role of user
     * @return Boolean
     */
    @Override
    public Boolean checkUserHasRole(Integer userId, RoleEnum roleEnum) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new ApiRequestException(Message.MESSAGE_086);
                });

        Set<RoleEnum> roleEnumSet = user.getRoles().stream().map(Roles::getRole).collect(Collectors.toSet());
        if (!roleEnumSet.contains(roleEnum)) {
            return false;
        }
        return true;
    }

    /**
     * This method use to check user has a specific role
     *
     * @param userId   is a user
     * @param roleEnum is enum use to check role of user
     * @return Boolean
     */
    @Override
    public User getUserHasRole(Integer userId, RoleEnum roleEnum) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new ApiRequestException(Message.MESSAGE_086);
                });

        Set<RoleEnum> roleEnumSet = user.getRoles().stream().map(Roles::getRole).collect(Collectors.toSet());
        if (!roleEnumSet.contains(roleEnum)) {
            throw new ApiRequestException(Message.MESSAGE_087 + roleEnum.getValue());
        }
        return user;
    }

    /**
     * This method use to get list user by status and role
     *
     * @param userStatusEnum is keyword to filter user list by status
     * @param roleEnum       is keyword to filter user list by role
     * @return
     */
    @Override
    public List<Object> getUserListByStatusAndRole(UserStatusEnum userStatusEnum, RoleEnum roleEnum) {
        return userRepository.getUserListByStatusAndRole(userStatusEnum.getKey(), roleEnum.getKey());
    }

    public UserResponseDto convertToDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setAccount(user.getAccount());
        userResponseDto.setEmail(user.getPerson().getEmail());
        userResponseDto.setPhoneNumber(user.getPerson().getPhoneNumber());
        userResponseDto.setStatus(user.getStatus().getValue());
        userResponseDto.setRoles(user.getRoles());
        return userResponseDto;
    }
}