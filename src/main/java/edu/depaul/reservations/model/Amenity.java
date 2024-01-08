package edu.depaul.reservations.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Amenity(
        Long id,
        String name,
        Long organizationId,
        Long addressId,
        AmenityType type,
        List<String> resources,
        Double rate,
        Set<String> daysAvailable,
        LocalTime timeAvailableStarting,
        LocalTime timeAvailableEnding,
        Integer transitionMinutes,
        String description,
        Long typeId,
        String resourcesString
) { }
