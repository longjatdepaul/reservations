package edu.depaul.reservations.api.users.repos;

import edu.depaul.reservations.api.addresses.model.Address;
import edu.depaul.reservations.api.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByFullNameIgnoreCase(String fullName);

    boolean existsByUsernameIgnoreCase(String username);

    Optional<User> findUserByUsername(String username);

    List<User> findByFullNameContainingIgnoreCase(String fullName);
}
