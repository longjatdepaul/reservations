package edu.depaul.reservations.api.amenities.repos;

import edu.depaul.reservations.api.amenities.model.AmenityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AmenityTypeRepository extends JpaRepository<AmenityType, Long> {

    boolean existsByNameIgnoreCase(String name);

    List<AmenityType> findByNameContainingIgnoreCase(String name);
}
