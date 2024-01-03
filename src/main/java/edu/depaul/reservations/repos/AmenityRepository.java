package edu.depaul.reservations.repos;

import edu.depaul.reservations.api.addresses.model.Address;
import edu.depaul.reservations.model.Amenity;
import edu.depaul.reservations.model.AmenityType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    boolean existsByNameIgnoreCase(String name);

    Amenity findFirstByAddress(Address address);

    @SuppressWarnings("unused")
    Amenity findByType(AmenityType amenityType);

}
