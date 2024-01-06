package edu.depaul.reservations.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Organization(
        Long id,
        String name,
        String contactUser,
        Long addressId
) { }
