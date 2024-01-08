package edu.depaul.reservations.api.addresses.service;

import edu.depaul.reservations.api.addresses.model.Address;
import edu.depaul.reservations.api.addresses.repos.AddressRepository;
import edu.depaul.reservations.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(final AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> findAll() {
        Pageable limit = PageRequest.of(0,15, Sort.by("id"));
        return addressRepository.findAll(limit).toList();
    }

    public List<Address> search(String query) {
        return addressRepository.findByNameContainingIgnoreCaseOrStreetContainingIgnoreCase(query, query);
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
}
