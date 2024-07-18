package com.example.mockproject.filter;


import com.example.mockproject.model.User;
import com.example.mockproject.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;


@Slf4j
@Component
public class TemporaryPasswordUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User user = userRepository.findUserByAccountIgnoreCase(account);
        if (user == null) {
            throw new UsernameNotFoundException("NO");
        }
        log.info("User found in database");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole().getValue()));
        });
        return new org.springframework.security.core.userdetails.User(user.getAccount(), user.getTemporaryPassword(), authorities);
    }
}
