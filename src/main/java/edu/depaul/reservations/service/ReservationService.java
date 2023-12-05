package edu.depaul.reservations.service;

import edu.depaul.reservations.domain.Amenity;
import edu.depaul.reservations.domain.Reservation;
import edu.depaul.reservations.domain.User;
import edu.depaul.reservations.model.ReservationDTO;
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

    public List<ReservationDTO> findAll() {
        final List<Reservation> reservations = reservationRepository.findAll(Sort.by("id"));
        return reservations.stream()
                .map(reservation -> mapToDTO(reservation, new ReservationDTO()))
                .toList();
    }

    public ReservationDTO get(final Long id) {
        return reservationRepository.findById(id)
                .map(reservation -> mapToDTO(reservation, new ReservationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReservationDTO reservationDTO) {
        final Reservation reservation = new Reservation();
        mapToEntity(reservationDTO, reservation);
        return reservationRepository.save(reservation).getId();
    }

    public void update(final Long id, final ReservationDTO reservationDTO) {
        final Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reservationDTO, reservation);
        reservationRepository.save(reservation);
    }

    public void delete(final Long id) {
        reservationRepository.deleteById(id);
    }

    private ReservationDTO mapToDTO(final Reservation reservation,
            final ReservationDTO reservationDTO) {
        reservationDTO.setId(reservation.getId());
        reservationDTO.setReservationDate(reservation.getReservationDate());
        reservationDTO.setStartTime(reservation.getStartTime());
        reservationDTO.setEndTime(reservation.getEndTime());
        reservationDTO.setUser(reservation.getUser() == null ? null : reservation.getUser().getId());
        reservationDTO.setAmenities(reservation.getAmenities().stream()
                .map(amenity -> amenity.getId())
                .toList());
        return reservationDTO;
    }

    private Reservation mapToEntity(final ReservationDTO reservationDTO,
            final Reservation reservation) {
        reservation.setReservationDate(reservationDTO.getReservationDate());
        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());
        final User user = reservationDTO.getUser() == null ? null : userRepository.findById(reservationDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        reservation.setUser(user);
        final List<Amenity> amenities = amenityRepository.findAllById(
                reservationDTO.getAmenities() == null ? Collections.emptyList() : reservationDTO.getAmenities());
        if (amenities.size() != (reservationDTO.getAmenities() == null ? 0 : reservationDTO.getAmenities().size())) {
            throw new NotFoundException("one of amenities not found");
        }
        reservation.setAmenities(amenities.stream().collect(Collectors.toSet()));
        return reservation;
    }

}
