package edu.depaul.reservations.api.users.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserTypeItem {

    private UserType id;
    private String text;
    private String slug;

}
