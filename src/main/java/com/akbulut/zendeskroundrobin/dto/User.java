package com.akbulut.zendeskroundrobin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User implements Comparable<User> {
    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("role")
    private String role;
    @JsonProperty("user_fields")
    private UserFields userFields;
    private int numOfTickets;

    @Override
    public int compareTo(User user) {
        return Integer.compare(this.getNumOfTickets(), user.numOfTickets);
    }

    @Data
    public class UserFields {
        @JsonProperty("active")
        private Boolean active;
        @JsonProperty("first_shift_monday")
        private Boolean firstShiftMonday;
        @JsonProperty("first_shift_tuesday")
        private Boolean firstShiftTuesday;
        @JsonProperty("first_shift_wednesday")
        private Boolean firstShiftWednesday;
        @JsonProperty("first_shift_thursday")
        private Boolean firstShiftThursday;
        @JsonProperty("first_shift_friday")
        private Boolean firstShiftFriday;

        @JsonProperty("second_shift_monday")
        private Boolean secondShiftMonday;
        @JsonProperty("second_shift_tuesday")
        private Boolean secondShiftTuesday;
        @JsonProperty("second_shift_wednesday")
        private Boolean secondShiftWednesday;
        @JsonProperty("second_shift_friday")
        private Boolean secondShiftFriday;
        @JsonProperty("second_shift_thursday")
        private Boolean secondShiftThursday;
    }

}
