package com.akbulut.zendeskroundrobin.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${api.username}")
    private String username;
    @Value("${api.token}")
    private String token;
    @Value("${first.shift.start.hour}")
    private int firstShiftStartHour;
    @Value("${first.shift.end.hour}")
    private int firstShiftEndHour;
    @Value("${second.shift.start.hour}")
    private int secondShiftStartHour;
    @Value("${second.shift.end.hour}")
    private int secondShiftEndHour;

    @Bean
    public String getUsername() {
        return username;
    }

    @Bean
    public String getToken() {
        return token;
    }

    @Bean
    public int getFirstShiftStartHour() {
        return firstShiftStartHour;
    }

    @Bean
    public int getFirstShiftEndHour() {
        return firstShiftEndHour;
    }

    @Bean
    public int getSecondShiftStartHour() {
        return secondShiftStartHour;
    }

    @Bean
    public int getSecondShiftEndHour() {
        return secondShiftEndHour;
    }
}
