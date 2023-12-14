package edu.depaul.reservations.repos;

import edu.depaul.reservations.model.Address;
import edu.depaul.reservations.model.Amenity;
import edu.depaul.reservations.model.Reservation;
import edu.depaul.reservations.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    boolean existsByNameIgnoreCase(String name);

    Amenity findFirstByAddress(Address address);

}
