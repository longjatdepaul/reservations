package edu.depaul.reservations.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AmenityType(
        Long id,
        String name,
        String description
) { }
