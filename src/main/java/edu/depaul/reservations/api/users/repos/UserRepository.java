package edu.depaul.reservations.api.users.repos;

import edu.depaul.reservations.api.users.model.Organization;
import edu.depaul.reservations.api.users.model.User;
import edu.depaul.reservations.api.users.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByFullNameIgnoreCase(String fullName);

    boolean existsByUsernameIgnoreCase(String username);

    Optional<User> findUserByUsername(String username);

    List<User> findAllByAddressId(Long addressId);

    List<User> findAllByType(UserType type);

    List<User> findAllByOrganization(Organization organization);

    List<User> findByFullNameContainingIgnoreCase(String fullName);
}
