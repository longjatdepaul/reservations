package edu.depaul.reservations.service;

import edu.depaul.reservations.model.Amenity;
import edu.depaul.reservations.model.Reservation;
import edu.depaul.reservations.model.User;
import edu.depaul.reservations.repos.AmenityRepository;
import edu.depaul.reservations.repos.ReservationRepository;
import edu.depaul.reservations.repos.UserRepository;
import edu.depaul.reservations.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AmenityRepository amenityRepository;

    public ReservationService(final ReservationRepository reservationRepository,
            final UserRepository userRepository, final AmenityRepository amenityRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.amenityRepository = amenityRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll(Sort.by("id"));
    }

    public Reservation get(final Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final Reservation reservation) {
        return reservationRepository.save(reservation).getId();
    }

    public void update(final Long id, final Reservation reservation) {
        reservationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        reservationRepository.save(reservation);
    }

    public void delete(final Long id) {
        reservationRepository.deleteById(id);
    }

}
