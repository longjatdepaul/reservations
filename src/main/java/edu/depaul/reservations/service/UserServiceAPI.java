package edu.depaul.reservations.service;

import com.google.gson.Gson;
import edu.depaul.reservations.model.User;
import edu.depaul.reservations.repos.ReservationRepository;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class UserServiceAPI {

    private final String userEndpoint;
    private final RestTemplate restTemplate;
    private final ReservationRepository reservationRepository;

    public UserServiceAPI(final @Value("${service.endpoint.users}") String userEndpoint,
                          final RestTemplate restTemplate,
                          final ReservationRepository reservationRepository) {
        this.userEndpoint = userEndpoint;
        this.restTemplate = restTemplate;
        this.reservationRepository = reservationRepository;
    }

    public List<User> findAll() {
        User[] users = restTemplate.getForObject(userEndpoint, User[].class);
        if (users == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(users);
        }
    }

    public User get(final String username) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        return restTemplate.getForObject(
                userEndpoint + "/{username}",
                User.class,
                map
        );
    }

    public String create(final User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new Gson().toJson(user);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        return restTemplate.postForObject(userEndpoint, request, String.class);
    }

    public void update(final String username, final User user) {
        // check that user exists, will throw NotFoundException
        get(username);
        Map<String, String> map = new HashMap<>();
        map.put("username", username);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new Gson().toJson(user);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        restTemplate.put(userEndpoint + "/{username}", request, map);
    }

    public void delete(final String username) {
        // check that user exists, will throw NotFoundException
        get(username);
        Map<String, String> map = new HashMap<>();
        map.put("username", username);

        restTemplate.delete(userEndpoint + "/{username}", map);
    }

    public Boolean fullNameExists(final String fullName) {
        Map<String, String> map = new HashMap<>();
        map.put("fullName", fullName);
        return restTemplate.getForObject(
                userEndpoint + "/exists?fullName={fullName}",
                Boolean.class,
                map
        );
    }

    public Boolean usernameExists(final String username) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        return restTemplate.getForObject(
                userEndpoint + "/exists?username={username}",
                Boolean.class,
                map
        );
    }

//    public String getReferencedWarning(final Long id) {
//        final User user = userRepository.findById(id)
//                .orElseThrow(NotFoundException::new);
//        final Reservation userReservation = reservationRepository.findFirstByUser(user);
//        if (userReservation != null) {
//            return WebUtils.getMessage("user.reservation.user.referenced", userReservation.getId());
//        }
//        return null;
//    }
}
