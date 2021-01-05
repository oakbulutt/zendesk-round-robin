package com.akbulut.zendeskroundrobin.wrapper;

import com.akbulut.zendeskroundrobin.dto.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWrapper {
    @JsonProperty("user")
    private User user;

    public UserWrapper() {
    }
}