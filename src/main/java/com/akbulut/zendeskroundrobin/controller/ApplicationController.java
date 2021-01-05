package com.akbulut.zendeskroundrobin.controller;

import com.akbulut.zendeskroundrobin.dto.Ticket;
import com.akbulut.zendeskroundrobin.dto.User;
import com.akbulut.zendeskroundrobin.service.TicketService;
import com.akbulut.zendeskroundrobin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApplicationController {
    private final UserService userService;
    private final TicketService ticketService;

    @GetMapping("/tickets")
    public List<Ticket> findAllTickets() {
        return ticketService.findAll();
    }

    public Ticket assignAgent(List<Ticket> tickets, List<Ticket> newTickets) {
        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Europe/Warsaw"));

        log.info("checkIfAllNew() runs: " + new Date());

        Ticket ticket = new Ticket();
        User user;

        List<User> users = userService.findAll(tickets);
        users = userService.activeAgents(users);

        switch (dateTime.getDayOfWeek()) {
            case MONDAY:
                user = userService.mondayAgents(users, dateTime);
                if (user == null) {
                    log.info("No available user for Monday " + new Date());
                    break;
                }
                ticket = ticketService.assignUser(user, newTickets.get(0));
                break;
            case TUESDAY:
                user = userService.tuesdayAgents(users, dateTime);
                if (user == null) {
                    log.info("No available user for Tuesday " + new Date());
                    break;
                }
                ticket = ticketService.assignUser(user, newTickets.get(0));
                break;
            case WEDNESDAY:
                user = userService.wednesdayAgents(users, dateTime);
                if (user == null) {
                    log.info("No available user for Wednesday " + new Date());
                    break;
                }
                ticket = ticketService.assignUser(user, newTickets.get(0));
                break;
            case THURSDAY:
                user = userService.thursdayAgents(users, dateTime);
                if (user == null) {
                    log.info("No available user for Thursday" + new Date());
                    break;
                }
                ticket = ticketService.assignUser(user, newTickets.get(0));
                break;
            case FRIDAY:
                user = userService.fridayAgents(users, dateTime);
                if (user == null) {
                    log.info("No available user for Friday" + new Date());
                    break;
                }
                ticket = ticketService.assignUser(user, newTickets.get(0));
                break;
            default:
                log.info("No available user for Weekend " + new Date());
                return null;
        }
        return ticket;
    }

    @Scheduled(cron = "0/3 * 8-22 * * MON-FRI")
    public void assignAgentSchedule() {
        log.info("assignAgentSchedule start: " + new Date());
        List<Ticket> tickets = findAllTickets();
        List<Ticket> newTickets = ticketService.checkIfAllNew(new ArrayList<>(tickets));
        if (!tickets.isEmpty() && !newTickets.isEmpty()) {
            assignAgent(tickets, newTickets);
            log.info("assignAgent() runs " + new Date());
        } else {
            log.info("No new tickets " + new Date());
        }
    }
}
