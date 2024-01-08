package edu.depaul.reservations.api.users.service;

import edu.depaul.reservations.api.users.model.Organization;
import edu.depaul.reservations.api.users.repos.OrganizationRepository;
import edu.depaul.reservations.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(final OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public List<Organization> findAll() {
        Pageable limit = PageRequest.of(0,15, Sort.by("id"));
        return organizationRepository.findAll(limit).toList();
    }

    public List<Organization> getAt(final Long addressId) {
        return organizationRepository.findAllByAddressId(addressId);
    }

    public List<Organization> getFor(final String username) {
        return organizationRepository.findAllByContactUser(username);
    }

    public List<Organization> search(String query) {
        return organizationRepository.findByNameContainingIgnoreCase(query);
    }

    public Organization get(final Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public Organization create(final Organization organization) {
        return organizationRepository.save(organization);
    }

    public void update(final Long id, final Organization organization) {
        organizationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        organizationRepository.save(organization);
    }

    public void delete(final Long id) {
        organizationRepository.deleteById(id);
    }

    public boolean nameExists(final String name) {
        return organizationRepository.existsByNameIgnoreCase(name);
    }
}
