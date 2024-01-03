package edu.depaul.reservations.api.addresses.repos;

import edu.depaul.reservations.api.addresses.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existsByNameIgnoreCase(String name);

}
