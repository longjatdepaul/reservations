package edu.depaul.reservations.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record User(
        Long id,
        String fullName,
        Long addressId,
        String username,
        String type,
        String passwordHash
) { }
