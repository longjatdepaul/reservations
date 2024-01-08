package edu.depaul.reservations.api.amenities.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DayOfWeekItem {

    private DayOfWeek id;
    private String text;

}
