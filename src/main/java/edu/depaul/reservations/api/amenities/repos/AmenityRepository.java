package edu.depaul.reservations.api.amenities.repos;

import edu.depaul.reservations.api.amenities.model.Amenity;
import edu.depaul.reservations.api.amenities.model.AmenityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    boolean existsByNameIgnoreCase(String name);

    List<Amenity> findAllByAddressId(Long addressId);

    List<Amenity> findAllByType(AmenityType type);

    List<Amenity> findAllByOrganizationId(Long organizationId);

    @SuppressWarnings("unused")
    Amenity findByType(AmenityType amenityType);

}
