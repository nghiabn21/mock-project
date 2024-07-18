package com.example.mockproject.processor;

import com.example.mockproject.dto.response.MessageResponseDto;
import com.example.mockproject.service.OfferService;
import com.example.mockproject.service.ScheduleService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduleProcessor {

    private final OfferService offerService;
    private final ScheduleService scheduleService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleStatusProcessor() {
        log.info("This scheduled task will set status for schedule and run at " + new Date());
        scheduleService.updateScheduleStatusEveryday();
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendMailReminderOffer() {
        offerService.sendEmailReminder
                (offerService.getAllOfferDueDate());
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void sendEmailRecruiterOwner() {
        scheduleService.sendScheduleRemind(
                scheduleService.getAllScheduleDateFrom());
    }
}
