package edu.depaul.reservations.repos;

import edu.depaul.reservations.model.Amenity;
import edu.depaul.reservations.model.Reservation;
import edu.depaul.reservations.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findFirstByUser(User user);

    Reservation findFirstByAmenities(Amenity amenity);

    List<Reservation> findAllByAmenities(Amenity amenity);

}
