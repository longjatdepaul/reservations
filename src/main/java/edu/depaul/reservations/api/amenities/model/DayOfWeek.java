package edu.depaul.reservations.api.amenities.model;

import lombok.Getter;

@Getter
public enum DayOfWeek {

    SUNDAY("Sunday"),
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday");

    private final String label;

    DayOfWeek(String label) {
        this.label = label;
    }
}
