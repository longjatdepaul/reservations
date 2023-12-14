package edu.depaul.reservations.repos;

import edu.depaul.reservations.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existsByNameIgnoreCase(String name);

}
