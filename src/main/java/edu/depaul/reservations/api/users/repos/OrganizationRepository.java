package edu.depaul.reservations.api.users.repos;

import edu.depaul.reservations.api.users.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    boolean existsByNameIgnoreCase(String name);

    List<Organization> findAllByAddressId(Long addressId);

    List<Organization> findAllByContactUser(String username);

    List<Organization> findByNameContainingIgnoreCase(String name);
}
