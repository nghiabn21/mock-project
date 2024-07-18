package com.example.mockproject.filter;

import com.example.mockproject.model.User;
import com.example.mockproject.repository.UserRepository;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User user = userRepository.findUserByAccountIgnoreCase(account);
        if (user == null) {
            throw new UsernameNotFoundException(Message.MESSAGE_002);
        }
        log.info("User found in database");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole().getValue()));
        });
        return new org.springframework.security.core.userdetails.User(user.getAccount(), user.getPassword(), authorities);

    }
}