package edu.depaul.reservations.api.users.model;

import lombok.Getter;

@Getter
public enum UserType {

    ADMIN("Administrator"),
    CUSTOMER("Customer"),
    OWNER("Owner");

    private final String label;

    UserType(String label) {
        this.label = label;
    }
}
