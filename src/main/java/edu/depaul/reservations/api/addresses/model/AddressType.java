package edu.depaul.reservations.api.addresses.model;

import lombok.Getter;

@Getter
public enum AddressType {

    BUSINESS("Business"),
    RESIDENTIAL("Residential");

    private final String label;

    AddressType(String label) {
        this.label = label;
    }
}
