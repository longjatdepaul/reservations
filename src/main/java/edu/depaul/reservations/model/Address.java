package edu.depaul.reservations.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Address(
        Long id,
        String name,
        String type,
        String street,
        String city,
        String state,
        String zip
) { }
