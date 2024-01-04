package edu.depaul.reservations.api.addresses.repos;

import edu.depaul.reservations.api.addresses.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existsByNameIgnoreCase(String name);

    List<Address> findByNameContainingIgnoreCaseOrStreetContainingIgnoreCase(String name, String street);
}
