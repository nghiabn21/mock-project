package com.example.mockproject.service;

import com.example.mockproject.dto.response.DataMailDto;
import javax.mail.MessagingException;

public interface EmailService {
    void sendMail(DataMailDto dataMail, String templateName) throws MessagingException;
    void sendMail(DataMailDto dataMail, String templateName,String filePath) throws MessagingException;
}
