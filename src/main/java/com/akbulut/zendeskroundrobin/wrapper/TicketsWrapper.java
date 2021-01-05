package com.akbulut.zendeskroundrobin.wrapper;

import com.akbulut.zendeskroundrobin.dto.Ticket;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class TicketsWrapper {
    @JsonProperty("tickets")
    @JsonAlias("results")
    List<Ticket> allTickets = new ArrayList<>();
    @JsonProperty("next_page")
    String nextPage;

    public TicketsWrapper() {
    }
}