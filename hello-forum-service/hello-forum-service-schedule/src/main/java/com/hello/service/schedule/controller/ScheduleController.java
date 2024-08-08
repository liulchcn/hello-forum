package com.hello.service.schedule.controller;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduleController {
    @Scheduled(cron = "0/2 * * * * ?")
    public void test(){
        System.out.println("1");
    }
}
