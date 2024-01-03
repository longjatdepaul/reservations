package edu.depaul.reservations.api.addresses.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StateItem {

    private StateType id;
    private String text;
    private String slug;

}
