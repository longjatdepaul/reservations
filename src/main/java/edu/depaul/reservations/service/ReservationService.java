package edu.depaul.reservations.service;

import edu.depaul.reservations.exception.CapacityFullException;
import edu.depaul.reservations.model.Reservation;
import edu.depaul.reservations.repos.CapacityRepository;
import edu.depaul.reservations.repos.ReservationRepository;
import edu.depaul.reservations.exception.NotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CapacityRepository capacityRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final CapacityRepository capacityRepository) {
        this.reservationRepository = reservationRepository;
        this.capacityRepository = capacityRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll(Sort.by("id"));
    }

    public Reservation get(final Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final Reservation reservation) {

        int capacity = capacityRepository.findByAmenityType(reservation.getAmenityType()).getCapacity();
        int overlappingReservations = reservationRepository
                .findReservationsByReservationDateAndStartTimeBeforeAndEndTimeAfterOrStartTimeBetween(
                        reservation.getReservationDate(),
                        reservation.getStartTime(), reservation.getEndTime(),
                        reservation.getStartTime(), reservation.getEndTime()).size();

        if (overlappingReservations >= capacity) {
            throw new CapacityFullException("This amenity's capacity is full at desired time");
        }

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
