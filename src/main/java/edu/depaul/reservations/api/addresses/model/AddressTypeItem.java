package edu.depaul.reservations.api.addresses.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressTypeItem {

    private AddressType id;
    private String text;

}
