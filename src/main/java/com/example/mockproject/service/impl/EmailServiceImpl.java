package com.example.mockproject.service.impl;

import com.example.mockproject.dto.response.DataMailDto;
import com.example.mockproject.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    /**
     * Using send email
     * @param dataMail - contains email sending fields
     * @param templateName - the name of the email form is in html
     * @throws MessagingException - throws an exception if send failed
     */
    @Override
    public void sendMail(DataMailDto dataMail, String templateName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        Context context = new Context();
        context.setVariables(dataMail.getProps());
        String html = templateEngine.process(templateName, context);
        helper.setTo(dataMail.getTo());
        helper.setSubject(dataMail.getSubject());
        helper.setText(html, true);
        mailSender.send(message);
    }

    /**
     *
     * @param dataMail - contains email sending fields
     * @param templateName - the name of the email form is in html
     * @param filePath - path of attachment
     * @throws MessagingException - throws an exception if send failed
     */
    @Override
    public void sendMail(DataMailDto dataMail, String templateName,String filePath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        Context context = new Context();
        context.setVariables(dataMail.getProps());
        String html = templateEngine.process(templateName, context);
        helper.setTo(dataMail.getTo());
        helper.setSubject(dataMail.getSubject());
        helper.setText(html, true);
        FileSystemResource file
                = new FileSystemResource(
                new File(filePath));
        if (file.exists()) {
            helper.addAttachment(
                    file.getFilename(), file);
        }

        mailSender.send(message);
    }

}