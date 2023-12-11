package edu.depaul.reservations.service;

import edu.depaul.reservations.domain.Reservation;
import edu.depaul.reservations.domain.User;
import edu.depaul.reservations.model.UserDTO;
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

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setUsername(user.getUsername());
        userDTO.setType(user.getType());
        userDTO.setPasswordHash(user.getPasswordHash());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setFullName(userDTO.getFullName());
        user.setUsername(userDTO.getUsername());
        user.setType(userDTO.getType());
        user.setPasswordHash(userDTO.getPasswordHash());
        return user;
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
