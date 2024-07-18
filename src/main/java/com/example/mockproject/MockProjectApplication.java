package com.example.mockproject;

import com.example.mockproject.configuration.FileProperties;
import org.apache.log4j.BasicConfigurator;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
@EnableConfigurationProperties({
        FileProperties.class
})
public class MockProjectApplication {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(MockProjectApplication.class, args);
    }


}
