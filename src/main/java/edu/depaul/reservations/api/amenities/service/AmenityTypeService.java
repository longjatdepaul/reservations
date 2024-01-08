package edu.depaul.reservations.api.amenities.service;

import edu.depaul.reservations.api.amenities.model.AmenityType;
import edu.depaul.reservations.api.amenities.repos.AmenityTypeRepository;
import edu.depaul.reservations.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AmenityTypeService {

    private final AmenityTypeRepository amenityTypeRepository;

    public AmenityTypeService(final AmenityTypeRepository amenityTypeRepository) {
        this.amenityTypeRepository = amenityTypeRepository;
    }

    public List<AmenityType> findAll() {
        Pageable limit = PageRequest.of(0,15, Sort.by("id"));
        return amenityTypeRepository.findAll(limit).toList();
    }

    public List<AmenityType> search(String query) {
        return amenityTypeRepository.findByNameContainingIgnoreCase(query);
    }

    public AmenityType get(final Long id) {
        return amenityTypeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public AmenityType create(final AmenityType amenityType) {
        return amenityTypeRepository.save(amenityType);
    }

    public void update(final Long id, final AmenityType amenityType) {
        amenityTypeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        amenityTypeRepository.save(amenityType);
    }

    public void delete(final Long id) {
        amenityTypeRepository.deleteById(id);
    }

    public boolean nameExists(final String name) {
        return amenityTypeRepository.existsByNameIgnoreCase(name);
    }
}
