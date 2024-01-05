package edu.depaul.reservations.api.users.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserTypeItem {

    private Long id;
    private String text;

}
