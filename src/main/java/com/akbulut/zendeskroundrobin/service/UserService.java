package com.akbulut.zendeskroundrobin.service;

import com.akbulut.zendeskroundrobin.configuration.AppConfig;
import com.akbulut.zendeskroundrobin.dto.Ticket;
import com.akbulut.zendeskroundrobin.dto.User;
import com.akbulut.zendeskroundrobin.wrapper.UsersWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UserService {
    public final String USERS = "https://COMPANY-DOMAIN.zendesk.com/api/v2/users.json?role[]=admin&role[]=agent";

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private TicketService ticketService;

    @Autowired
    private AppConfig config;


    public HttpEntity request() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBasicAuth(config.getUsername(), config.getToken());

        return new HttpEntity(requestHeaders);
    }

    public List<User> findAll(List<Ticket> tickets) {
        UsersWrapper claims = new UsersWrapper();

        try {
            ResponseEntity<UsersWrapper> claimResponse = restTemplate.exchange(
                    USERS,
                    HttpMethod.GET,
                    request(),
                    new ParameterizedTypeReference<UsersWrapper>() {
                    });
            if (claimResponse != null && claimResponse.hasBody()) {
                claims = claimResponse.getBody();
            }
        } catch (
                RestClientException e) {
            e.printStackTrace();
        }
        log.info("users findAll() returns user list " + new Date());
        return setAgentsNumOfOpenTickets(claims.getUsers(), tickets);
    }

    public List<User> activeAgents(List<User> users) {
        Iterator<User> iterator = users.listIterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (!user.getUserFields().getActive()) {
                iterator.remove();
            }
        }
        log.info("activeAgents() returns user list " + new Date());
        return users;
    }

    public User mondayAgents(List<User> users, LocalDateTime localDateTime) {
        List<User> firstShiftUsers = new ArrayList<>();
        List<User> secondShiftUsers = new ArrayList<>();

        Iterator<User> iterator = users.listIterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getUserFields().getFirstShiftMonday()) {
                firstShiftUsers.add(user);
            } else if (user.getUserFields().getSecondShiftMonday()) {
                secondShiftUsers.add(user);
            }
        }

        User firstShiftTicketAssignee = findAgentWithLeastNumOfTickets(firstShiftUsers);
        User secondShiftTicketAssignee = findAgentWithLeastNumOfTickets(secondShiftUsers);

        return findAssignee(firstShiftTicketAssignee, secondShiftTicketAssignee, localDateTime);
    }

    public User tuesdayAgents(List<User> users, LocalDateTime localDateTime) {
        List<User> firstShiftUsers = new ArrayList<>();
        List<User> secondShiftUsers = new ArrayList<>();

        Iterator<User> iterator = users.listIterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getUserFields().getFirstShiftTuesday()) {
                firstShiftUsers.add(user);

            } else if (user.getUserFields().getSecondShiftTuesday()) {
                secondShiftUsers.add(user);
            }
        }

        User firstShiftTicketAssignee = findAgentWithLeastNumOfTickets(firstShiftUsers);
        User secondShiftTicketAssignee = findAgentWithLeastNumOfTickets(secondShiftUsers);

        return findAssignee(firstShiftTicketAssignee, secondShiftTicketAssignee, localDateTime);
    }

    public User wednesdayAgents(List<User> users, LocalDateTime localDateTime) {
        List<User> firstShiftUsers = new ArrayList<>();
        List<User> secondShiftUsers = new ArrayList<>();

        Iterator<User> iterator = users.listIterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getUserFields().getFirstShiftWednesday()) {
                firstShiftUsers.add(user);
            } else if (user.getUserFields().getSecondShiftWednesday()) {
                secondShiftUsers.add(user);
            }
        }

        User firstShiftTicketAssignee = findAgentWithLeastNumOfTickets(firstShiftUsers);
        User secondShiftTicketAssignee = findAgentWithLeastNumOfTickets(secondShiftUsers);

        return findAssignee(firstShiftTicketAssignee, secondShiftTicketAssignee, localDateTime);
    }

    public User thursdayAgents(List<User> users, LocalDateTime localDateTime) {
        List<User> firstShiftUsers = new ArrayList<>();
        List<User> secondShiftUsers = new ArrayList<>();

        Iterator<User> iterator = users.listIterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getUserFields().getFirstShiftThursday()) {
                firstShiftUsers.add(user);
            } else if (user.getUserFields().getSecondShiftThursday()) {
                secondShiftUsers.add(user);
            }
        }

        User firstShiftTicketAssignee = findAgentWithLeastNumOfTickets(firstShiftUsers);
        User secondShiftTicketAssignee = findAgentWithLeastNumOfTickets(secondShiftUsers);

        return findAssignee(firstShiftTicketAssignee, secondShiftTicketAssignee, localDateTime);
    }

    public User fridayAgents(List<User> users, LocalDateTime localDateTime) {
        List<User> firstShiftUsers = new ArrayList<>();
        List<User> secondShiftUsers = new ArrayList<>();

        Iterator<User> iterator = users.listIterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getUserFields().getFirstShiftFriday()) {
                firstShiftUsers.add(user);
            } else if (user.getUserFields().getSecondShiftFriday()) {
                secondShiftUsers.add(user);
            }
        }

        User firstShiftTicketAssignee = findAgentWithLeastNumOfTickets(firstShiftUsers);
        User secondShiftTicketAssignee = findAgentWithLeastNumOfTickets(secondShiftUsers);

        return findAssignee(firstShiftTicketAssignee, secondShiftTicketAssignee, localDateTime);
    }

    private User findAssignee(User firstShiftUser, User secondShiftUser, LocalDateTime localDateTime) {
        if (localDateTime.getHour() >= config.getFirstShiftStartHour()
                && localDateTime.getHour() < config.getSecondShiftStartHour()) {
            return firstShiftUser;
        } else if (localDateTime.getHour() >= config.getFirstShiftEndHour()
                && localDateTime.getHour() < config.getSecondShiftEndHour()) {
            return secondShiftUser;
        } else if (localDateTime.getHour() >= config.getSecondShiftStartHour()
                && localDateTime.getHour() < config.getFirstShiftEndHour()) {
            if (firstShiftUser.getNumOfTickets() < secondShiftUser.getNumOfTickets()) {
                return firstShiftUser;
            } else {
                return secondShiftUser;
            }
        } else {
            log.info("Shifts finished " + new Date());
            return null;
        }
    }

    private List<User> setAgentsNumOfOpenTickets(List<User> users, List<Ticket> tickets) {
        List<Ticket> ticketList = ticketService.checkIfOpen(tickets);
        for (Ticket t : ticketList) {
            for (User u : users) {
                if (t.getAssigneeId() == u.getId() && t.getStatus().equals("open")) {
                    u.setNumOfTickets(u.getNumOfTickets() + 1);
                }
            }
        }
        log.info("setAgentsNumOfOpenTickets() returns user list " + new Date());
        return users;
    }

    private User findAgentWithLeastNumOfTickets(List<User> users) {
        List<User> userList = users;
        Collections.sort(userList);
        return userList.get(0);
    }
}
