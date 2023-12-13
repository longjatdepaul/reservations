package edu.depaul.reservations.service;

import edu.depaul.reservations.model.Reservation;
import edu.depaul.reservations.model.User;
import edu.depaul.reservations.repos.ReservationRepository;
import edu.depaul.reservations.repos.UserRepository;
import edu.depaul.reservations.util.NotFoundException;
import edu.depaul.reservations.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public UserService(final UserRepository userRepository,
            final ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll(Sort.by("id"));
    }

    public User get(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final User user) {
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final User user) {
        userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    public boolean fullNameExists(final String fullName) {
        return userRepository.existsByFullNameIgnoreCase(fullName);
    }

    public boolean usernameExists(final String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    public String getReferencedWarning(final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Reservation userReservation = reservationRepository.findFirstByUser(user);
        if (userReservation != null) {
            return WebUtils.getMessage("user.reservation.user.referenced", userReservation.getId());
        }
        return null;
    }

}
