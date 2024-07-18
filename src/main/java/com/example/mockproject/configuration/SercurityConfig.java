package com.example.mockproject.configuration;

import com.example.mockproject.filter.CustomAuthenticationFilter;
import com.example.mockproject.filter.CustomAuthorizationFilter;
import com.example.mockproject.filter.JwtUserDetails;
import com.example.mockproject.filter.TemporaryPasswordUserDetailsService;
import com.example.mockproject.service.UserService;
import com.example.mockproject.utils.handle_exception.MyAccessDeniedHandler;
import com.example.mockproject.utils.handle_exception.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SercurityConfig {
    @Autowired
    JwtUserDetails userDetails;
    @Autowired
    RestAuthenticationEntryPoint authEntryPoint;
    @Autowired
    UserService userService;
    @Autowired
    TemporaryPasswordUserDetailsService userDetailsService;
    @Autowired
    AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetails).passwordEncoder(passwordEncoder);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager(), userService);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                //LOGIN
                .antMatchers(POST,"/api/login/**").permitAll()
                .antMatchers(POST, "/api/reset_password/**").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER", "ROLE_INTERVIEWER")
                .antMatchers(POST, "/api/forgot_password/**").permitAll()
                //CANDIDATE
                .antMatchers(POST, "/api/candidate/create-candidate").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER")
                .antMatchers(GET, "/api/candidate/candidate-list").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER", "ROLE_INTERVIEWER")
                .antMatchers(PUT, "/api/candidate/edit-candidate-ID/**").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER")
                .antMatchers(GET, "/api/candidate/view-candidate-ID/**").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER", "ROLE_INTERVIEWER")
                .antMatchers(DELETE, "/api/candidate/delete/**").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER")
                //JOB
                .antMatchers(GET, "/api/job/job-list").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER")
                .antMatchers(GET, "/api/job/get-allJob").hasAnyAuthority( "ROLE_INTERVIEWER")
                .antMatchers(POST, "/api/job/create-job").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER")
                .antMatchers(GET, "/api/job/get-detail/**").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER", "ROLE_INTERVIEWER")
                .antMatchers(PUT, "/api/job/edit-job/**").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER")
                .antMatchers(POST, "/api/job/delete-job/**").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER")
                //INTERVIEW
                .antMatchers(POST, "/api/v1/interview-schedule-list-grid").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER", "ROLE_INTERVIEWER")
                .antMatchers(POST, "/api/v1/create-interview-schedule").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER")
                .antMatchers(GET, "/api/v1/create-interview-schedule").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER")
                .antMatchers(GET, "/api/v1/view-interview-schedule/**").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER")
                .antMatchers(POST, "/api/v1/view-interview-schedule/submit/**").hasAnyAuthority("ROLE_INTERVIEWER")
                .antMatchers(PUT, "/api/v1/schedule/edit-interview-schedule/**").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER")
                .antMatchers(GET, "/api/v1/get-interviewer-schedule/**").hasAnyAuthority("ROLE_INTERVIEWER")
                //offer
                .antMatchers(GET, "/api/v1/offer/view-offer").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER")
                .antMatchers(POST, "/api/v1/offer/create-offer").hasAnyAuthority("ROLE_RECRUITER", "ROLE_MANAGER")
                .antMatchers(GET, "/api/v1/offer/get-current-offer").hasAnyAuthority("ROLE_MANAGER")
                .antMatchers(POST, "/api/excel/export/offer-excel").hasAnyAuthority("ROLE_RECRUITER","ROLE_MANAGER")
                .antMatchers(PUT, "/api/v1/offer/**").hasAnyAuthority("ROLE_RECRUITER")
                .antMatchers(GET, "/api/v1/offer/**").hasAnyAuthority("ROLE_RECRUITER")
                .antMatchers(POST, "/api/v1/offer/change-offer-status/**").hasAnyAuthority("ROLE_MANAGER")

                //USER
                .antMatchers(GET, "/api/search-user").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(POST, "/api/create-user").hasAnyAuthority("ROLE_ADMIN")

                .anyRequest().authenticated();
        http.exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler());
        http.exceptionHandling().authenticationEntryPoint(authEntryPoint);
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

