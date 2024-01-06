package edu.depaul.reservations.api.users.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserItem {

    private String id;
    private String text;

}
