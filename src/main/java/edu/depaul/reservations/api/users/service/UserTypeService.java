package edu.depaul.reservations.api.users.service;

import edu.depaul.reservations.api.users.model.UserType;
import edu.depaul.reservations.api.users.repos.UserTypeRepository;
import edu.depaul.reservations.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserTypeService {

    private final UserTypeRepository userTypeRepository;

    public UserTypeService(final UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    public List<UserType> findAll() {
        Pageable limit = PageRequest.of(0,15, Sort.by("id"));
        return userTypeRepository.findAll(limit).toList();
    }

    public List<UserType> search(String query) {
        return userTypeRepository.findByNameContainingIgnoreCase(query);
    }

    public UserType get(final Long id) {
        return userTypeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public UserType create(final UserType userType) {
        return userTypeRepository.save(userType);
    }

    public void update(final Long id, final UserType userType) {
        userTypeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userTypeRepository.save(userType);
    }

    public void delete(final Long id) {
        userTypeRepository.deleteById(id);
    }

    public boolean nameExists(final String name) {
        return userTypeRepository.existsByNameIgnoreCase(name);
    }
}
