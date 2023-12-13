package edu.depaul.reservations.repos;

import edu.depaul.reservations.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByFullNameIgnoreCase(String fullName);

    boolean existsByUsernameIgnoreCase(String username);

}
