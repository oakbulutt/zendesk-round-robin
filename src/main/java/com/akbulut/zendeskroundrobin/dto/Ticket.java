package com.akbulut.zendeskroundrobin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Ticket {
    @JsonProperty("id")
    private long id;
    @JsonProperty("status")
    private String status;
    @JsonProperty("assignee_id")
    private long assigneeId;
    @JsonProperty("recipient")
    private String recipient;
}
