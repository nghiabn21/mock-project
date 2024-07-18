package com.example.mockproject.dto.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataMailDto {
    private String to;
    private String subject;
    private String content;
    private String attachment;
    private Map<String, Object> props;
}
