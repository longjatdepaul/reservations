package edu.depaul.reservations.api.amenities.service;

import edu.depaul.reservations.api.amenities.model.Amenity;
import edu.depaul.reservations.api.amenities.model.AmenityType;
import edu.depaul.reservations.api.amenities.repos.AmenityRepository;
import edu.depaul.reservations.exception.NotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class AmenityService {

    private final AmenityRepository amenityRepository;

    public AmenityService(final AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    public List<Amenity> findAll() {
        return amenityRepository.findAll(Sort.by("id"));
    }

    public List<Amenity> getAt(final Long addressId) {
        return amenityRepository.findAllByAddressId(addressId);
    }

    public List<Amenity> getOf(final AmenityType amenityType) {
        return amenityRepository.findAllByType(amenityType);
    }

    public List<Amenity> getIn(final Long organizationId) {
        return amenityRepository.findAllByOrganizationId(organizationId);
    }

    public Amenity get(final Long id) {
        return amenityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final Amenity amenity) {
        return amenityRepository.save(amenity).getId();
    }

    public void update(final Long id, final Amenity amenity) {
        amenityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        amenityRepository.save(amenity);
    }

    public void delete(final Long id) {
        final Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        amenityRepository.delete(amenity);
    }

    public boolean nameExists(final String name) {
        return amenityRepository.existsByNameIgnoreCase(name);
    }
}
