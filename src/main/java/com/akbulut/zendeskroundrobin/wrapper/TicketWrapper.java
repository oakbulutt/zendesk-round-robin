package com.akbulut.zendeskroundrobin.wrapper;

import com.akbulut.zendeskroundrobin.dto.Ticket;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TicketWrapper {
    @JsonProperty("ticket")
    Ticket ticket;

    public TicketWrapper() {
    }

    public TicketWrapper(Ticket ticket) {
        this.ticket = ticket;
    }
}