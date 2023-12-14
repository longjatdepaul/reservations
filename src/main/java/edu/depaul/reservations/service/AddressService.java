package edu.depaul.reservations.service;

import edu.depaul.reservations.model.Address;
import edu.depaul.reservations.model.Amenity;
import edu.depaul.reservations.repos.AddressRepository;
import edu.depaul.reservations.repos.AmenityRepository;
import edu.depaul.reservations.util.NotFoundException;
import edu.depaul.reservations.util.WebUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AmenityRepository amenityRepository;

    public AddressService(final AddressRepository addressRepository,
                          final AmenityRepository amenityRepository) {
        this.addressRepository = addressRepository;
        this.amenityRepository = amenityRepository;
    }

    public List<Address> findAll() {
        return addressRepository.findAll(Sort.by("id"));
    }

    public Address get(final Long id) {
        return addressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final Address address) {
        return addressRepository.save(address).getId();
    }

    public void update(final Long id, final Address address) {
        addressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        addressRepository.save(address);
    }

    public void delete(final Long id) {
        addressRepository.deleteById(id);
    }

    public boolean nameExists(final String name) {
        return addressRepository.existsByNameIgnoreCase(name);
    }

    public String getReferencedWarning(final Long id) {
        final Address address = addressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Amenity amenity = amenityRepository.findFirstByAddress(address);
        if (amenity != null) {
            return WebUtils.getMessage("address.amenity.address.referenced", amenity.getId());
        }
        return null;
    }

}
