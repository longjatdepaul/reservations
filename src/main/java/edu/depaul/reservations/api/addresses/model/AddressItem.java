package edu.depaul.reservations.api.addresses.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressItem {

    private Long id;
    private String text;
    private String slug;

}
