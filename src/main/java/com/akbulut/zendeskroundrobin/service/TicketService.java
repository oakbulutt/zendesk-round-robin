package com.akbulut.zendeskroundrobin.service;

import com.akbulut.zendeskroundrobin.configuration.AppConfig;
import com.akbulut.zendeskroundrobin.dto.Ticket;
import com.akbulut.zendeskroundrobin.dto.User;
import com.akbulut.zendeskroundrobin.wrapper.TicketWrapper;
import com.akbulut.zendeskroundrobin.wrapper.TicketsWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class TicketService {
    public static final String TICKETS = "https://COMPANY-DOMAIN.zendesk.com/api/v2/tickets";
    public static final String TICKET = "https://COMPANY-DOMAIN.zendesk.com/api/v2/tickets/{id}";

    private final RestTemplate restTemplate = new RestTemplate();
    private TicketsWrapper ticketsWrapper;

    @Autowired
    private AppConfig config;


    private HttpEntity request() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBasicAuth(config.getUsername(), config.getToken());
        HttpEntity requestEntity = new HttpEntity(requestHeaders);

        return requestEntity;
    }

    public List<Ticket> findAll() {
        String url = TICKETS;
        List<Ticket> tickets = new ArrayList<>();
        while (url != null) {
            try {
                ResponseEntity<TicketsWrapper> claimResponse = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        request(),
                        new ParameterizedTypeReference<TicketsWrapper>() {
                        });
                if (claimResponse != null && claimResponse.hasBody()) {
                    ticketsWrapper = claimResponse.getBody();
                }
            } catch (
                    RestClientException e) {
                e.printStackTrace();
            }
            tickets.addAll(ticketsWrapper.getAllTickets());
            url = ticketsWrapper.getNextPage();
        }
        log.info("ticket findAll() returns ticket list " + new Date());
        return checkRecipient(tickets);
    }

    // TO REMOVE TICKETS FOR A SPECIFIC RECIPIENT
    public List<Ticket> checkRecipient(List<Ticket> tickets) {
        Iterator<Ticket> iterator = tickets.listIterator();
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();
            if (ticket.getRecipient() != null &&
                    ticket.getRecipient().equals("RECIPIENT EMAIL ADDRESS HERE")) {
                iterator.remove();
            }
        }
        return tickets;
    }

    public List<Ticket> checkIfAllNew(List<Ticket> tickets) {
        Iterator<Ticket> iterator = tickets.listIterator();
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();
            if (!ticket.getStatus().equals("new")) {
                iterator.remove();
            }
        }
        log.info("checkIfAllNew() returns new ticket list " + new Date());
        return tickets;
    }

    public List<Ticket> checkIfOpen(List<Ticket> tickets) {
        Iterator<Ticket> iterator = tickets.listIterator();
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();

            if (ticket.getStatus().equals("closed") ||
                    ticket.getStatus().equals("solved")) {
                iterator.remove();
            }
        }
        return tickets;
    }

    public Ticket assignUser(User user, Ticket ticketToUpdate) {

        ticketToUpdate.setAssigneeId(user.getId());

        TicketWrapper ticketWrapper = new TicketWrapper(ticketToUpdate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(config.getUsername(), config.getToken());
        HttpEntity<TicketWrapper> entity = new HttpEntity<>(ticketWrapper, headers);

        ResponseEntity<TicketWrapper> responseEntity = restTemplate.exchange(
                TICKET,
                HttpMethod.PUT,
                entity,
                TicketWrapper.class,
                ticketToUpdate.getId());

        return responseEntity.getBody().getTicket();
    }

}
