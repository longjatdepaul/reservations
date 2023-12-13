package edu.depaul.reservations.repos;

import edu.depaul.reservations.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    boolean existsByNameIgnoreCase(String name);

}
