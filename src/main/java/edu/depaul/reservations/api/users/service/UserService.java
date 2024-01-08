package edu.depaul.reservations.api.users.service;

import edu.depaul.reservations.api.users.model.Organization;
import edu.depaul.reservations.api.users.model.UserType;
import edu.depaul.reservations.exception.NotFoundException;
import edu.depaul.reservations.api.users.model.User;
import edu.depaul.reservations.api.users.repos.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        Pageable limit = PageRequest.of(0,15, Sort.by("id"));
        return userRepository.findAll(limit).toList();
    }

    public List<User> getAt(final Long addressId) {
        return userRepository.findAllByAddressId(addressId);
    }

    public List<User> getOf(final UserType userType) {
        return userRepository.findAllByType(userType);
    }

    public List<User> getIn(final Organization organization) {
        return userRepository.findAllByOrganization(organization);
    }

    public List<User> search(String query) {
        return userRepository.findByFullNameContainingIgnoreCase(query);
    }

    public User get(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public User get(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(NotFoundException::new);
    }

    public User create(final User user) {
        return userRepository.save(user);
    }

    public void update(final Long id, final User user) {
        userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userRepository.save(user);
    }

    public void update(final String username, final User user) {
        userRepository.findUserByUsername(username)
                .orElseThrow(NotFoundException::new);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    public void delete(final String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(NotFoundException::new);
        userRepository.deleteById(user.getId());
    }

    public boolean fullNameExists(final String fullName) {
        return userRepository.existsByFullNameIgnoreCase(fullName);
    }

    public boolean usernameExists(final String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

}
