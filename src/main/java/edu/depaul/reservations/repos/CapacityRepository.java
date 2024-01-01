package edu.depaul.reservations.repos;

import edu.depaul.reservations.model.AmenityType;
import edu.depaul.reservations.model.Capacity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapacityRepository extends JpaRepository<Capacity, Long> {

    Capacity findByAmenityType(AmenityType amenityType);
}
