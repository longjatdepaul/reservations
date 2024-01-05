package edu.depaul.reservations.api.users.repos;

import edu.depaul.reservations.api.users.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserTypeRepository extends JpaRepository<UserType, Long> {

    boolean existsByNameIgnoreCase(String name);

    List<UserType> findByNameContainingIgnoreCase(String name);
}
