package edu.depaul.reservations.service;

import edu.depaul.reservations.model.Amenity;
import edu.depaul.reservations.model.Reservation;
import edu.depaul.reservations.repos.AmenityRepository;
import edu.depaul.reservations.repos.ReservationRepository;
import edu.depaul.reservations.util.NotFoundException;
import edu.depaul.reservations.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class AmenityService {

    private final AmenityRepository amenityRepository;
    private final ReservationRepository reservationRepository;

    public AmenityService(final AmenityRepository amenityRepository,
            final ReservationRepository reservationRepository) {
        this.amenityRepository = amenityRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Amenity> findAll() {
        return amenityRepository.findAll(Sort.by("id"));
    }

    public Amenity get(final Long id) {
        return amenityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final Amenity amenity) {
        return amenityRepository.save(amenity).getId();
    }

    public void update(final Long id, final Amenity amenity) {
        amenityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        amenityRepository.save(amenity);
    }

    public void delete(final Long id) {
        final Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        reservationRepository.findAllByAmenities(amenity)
                .forEach(reservation -> reservation.getAmenities().remove(amenity));
        amenityRepository.delete(amenity);
    }

    public boolean nameExists(final String name) {
        return amenityRepository.existsByNameIgnoreCase(name);
    }

    public String getReferencedWarning(final Long id) {
        final Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Reservation amenitiesReservation = reservationRepository.findFirstByAmenities(amenity);
        if (amenitiesReservation != null) {
            return WebUtils.getMessage("amenity.reservation.amenities.referenced", amenitiesReservation.getId());
        }
        return null;
    }

}
