package edu.depaul.reservations.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record User(
        Long id,
        String fullName,
        String username,
        UserType type,
        String passwordHash,
        String email,
        String mobile,
        Long addressId,
        Organization organization,
        Long typeId,
        Long organizationId
) { }
