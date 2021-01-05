package com.akbulut.zendeskroundrobin.wrapper;

import com.akbulut.zendeskroundrobin.dto.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class UsersWrapper {
    List<User> users = new ArrayList<>();

    public UsersWrapper() {
    }
}