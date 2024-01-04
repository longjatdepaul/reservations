package edu.depaul.reservations.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record User(
        String fullName,
        String username,
        String type,
        String passwordHash
) { }
