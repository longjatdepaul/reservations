package edu.depaul.reservations.service;

import com.google.gson.Gson;
import edu.depaul.reservations.model.User;
import edu.depaul.reservations.model.UserType;
import edu.depaul.reservations.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserTypeServiceAPI {

    private final String userTypeEndpoint;
    private final RestTemplate restTemplate;
    private final UserServiceAPI userService;

    public UserTypeServiceAPI(final @Value("${service.endpoint.userTypes}") String userTypeEndpoint,
                              final RestTemplate restTemplate,
                              final UserServiceAPI userService) {
        this.userTypeEndpoint = userTypeEndpoint;
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    public List<UserType> findAll() {
        UserType[] userTypes = restTemplate.getForObject(userTypeEndpoint, UserType[].class);
        if (userTypes == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(userTypes);
        }
    }

    public UserType get(final Long id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());
        return restTemplate.getForObject(
                userTypeEndpoint + "/{id}",
                UserType.class,
                map
        );
    }

    public UserType create(final UserType userType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new Gson().toJson(userType);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        return restTemplate.postForObject(userTypeEndpoint, request, UserType.class);
    }

    public void update(final Long id, final UserType userType) {
        // check that user type exists, will throw NotFoundException
        get(id);
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new Gson().toJson(userType);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        restTemplate.put(userTypeEndpoint + "/{id}", request, map);
    }

    public void delete(final Long id) {
        // check that user type exists, will throw NotFoundException
        get(id);
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());

        restTemplate.delete(userTypeEndpoint + "/{id}", map);
    }

    public boolean nameExists(final String name) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        return Boolean.TRUE.equals(restTemplate.getForObject(
                userTypeEndpoint + "/exists?name={name}",
                Boolean.class,
                map
        ));
    }

    public String getReferencedWarning(final Long id) {
        // check that user type exists, will throw NotFoundException
        final UserType userType = get(id);
        final List<User> users = userService.findByUserType(userType.id());
        if (!users.isEmpty()) {
            return WebUtils.getMessage("userType.user.userType.referenced",
                    Arrays.toString(users.stream().map(User::id).toArray())
            );
        }
        return null;
    }
}