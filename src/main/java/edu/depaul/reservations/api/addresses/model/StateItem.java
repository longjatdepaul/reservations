package edu.depaul.reservations.api.addresses.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StateItem {

    private State id;
    private String text;

}
