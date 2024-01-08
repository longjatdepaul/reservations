package edu.depaul.reservations.api.amenities.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AmenityTypeItem {

    private Long id;
    private String text;

}
