package edu.depaul.reservations.service;

import com.google.gson.Gson;
import edu.depaul.reservations.model.Organization;
import edu.depaul.reservations.model.User;

import java.util.*;

import edu.depaul.reservations.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceAPI {

    private final String userEndpoint;
    private final RestTemplate restTemplate;
    private final OrganizationServiceAPI organizationService;

    public UserServiceAPI(final @Value("${service.endpoint.users}") String userEndpoint,
                          final RestTemplate restTemplate,
                          @Lazy final OrganizationServiceAPI organizationService) {
        this.userEndpoint = userEndpoint;
        this.restTemplate = restTemplate;
        this.organizationService = organizationService;
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

    public List<User> findByAddress(final Long addressId) {
        Map<String, String> map = new HashMap<>();
        map.put("addressId", addressId.toString());
        User[] users = restTemplate.getForObject(
                userEndpoint + "/at/{addressId}",
                User[].class,
                map
        );
        if (users == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(users);
        }
    }

    public List<User> findByUserType(final Long typeId) {
        Map<String, String> map = new HashMap<>();
        map.put("typeId", typeId.toString());
        User[] users = restTemplate.getForObject(
                userEndpoint + "/of/{typeId}",
                User[].class,
                map
        );
        if (users == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(users);
        }
    }

    public List<User> findByOrganization(final Long organizationId) {
        Map<String, String> map = new HashMap<>();
        map.put("organizationId", organizationId.toString());
        User[] users = restTemplate.getForObject(
                userEndpoint + "/in/{organizationId}",
                User[].class,
                map
        );
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

    public User create(final User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new Gson().toJson(user);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        return restTemplate.postForObject(userEndpoint, request, User.class);
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
        return Boolean.TRUE.equals(restTemplate.getForObject(
                userEndpoint + "/exists?username={username}",
                Boolean.class,
                map
        ));
    }

    public String getReferencedWarning(final String username) {
        // check that user exists, will throw NotFoundException
        final User user = get(username);
        final List<Organization> organizations = organizationService.findByContactUser(user.username());
        if (!organizations.isEmpty()) {
            return WebUtils.getMessage("user.organization.user.referenced",
                    Arrays.toString(organizations.stream().map(Organization::id).toArray())
            );
        }
        return null;
    }
}
