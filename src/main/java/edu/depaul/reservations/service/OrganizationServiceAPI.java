package edu.depaul.reservations.service;

import com.google.gson.Gson;
import edu.depaul.reservations.model.Address;
import edu.depaul.reservations.model.Amenity;
import edu.depaul.reservations.model.Organization;
import edu.depaul.reservations.model.User;
import edu.depaul.reservations.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OrganizationServiceAPI {

    private final String organizationEndpoint;
    private final RestTemplate restTemplate;
    private final AmenityServiceAPI amenityService;
    private final UserServiceAPI userService;

    public OrganizationServiceAPI(final @Value("${service.endpoint.organizations}") String organizationEndpoint,
                                  final RestTemplate restTemplate,
                                  final AmenityServiceAPI amenityService,
                                  final UserServiceAPI userService) {
        this.organizationEndpoint = organizationEndpoint;
        this.restTemplate = restTemplate;
        this.amenityService = amenityService;
        this.userService = userService;
    }

    public List<Organization> findAll() {
        Organization[] organizations = restTemplate.getForObject(organizationEndpoint, Organization[].class);
        if (organizations == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(organizations);
        }
    }

    public List<Organization> findByAddress(final Long addressId) {
        Map<String, String> map = new HashMap<>();
        map.put("addressId", addressId.toString());
        Organization[] organizations = restTemplate.getForObject(
                organizationEndpoint + "/at/{addressId}",
                Organization[].class,
                map
        );
        if (organizations == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(organizations);
        }
    }

    public List<Organization> findByContactUser(final String username) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        Organization[] organizations = restTemplate.getForObject(
                organizationEndpoint + "/for/{username}",
                Organization[].class,
                map
        );
        if (organizations == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(organizations);
        }
    }

    public Organization get(final Long id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());
        return restTemplate.getForObject(
                organizationEndpoint + "/{id}",
                Organization.class,
                map
        );
    }

    public Organization create(final Organization organization) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new Gson().toJson(organization);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        return restTemplate.postForObject(organizationEndpoint, request, Organization.class);
    }

    public void update(final Long id, final Organization organization) {
        // check that organization exists, will throw NotFoundException
        get(id);
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new Gson().toJson(organization);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        restTemplate.put(organizationEndpoint + "/{id}", request, map);
    }

    public void delete(final Long id) {
        // check that organization exists, will throw NotFoundException
        get(id);
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());

        restTemplate.delete(organizationEndpoint + "/{id}", map);
    }

    public boolean nameExists(final String name) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        return Boolean.TRUE.equals(restTemplate.getForObject(
                organizationEndpoint + "/exists?name={name}",
                Boolean.class,
                map
        ));
    }

    public String getReferencedWarning(final Long id) {
        // check that address exists, will throw NotFoundException
        final Organization organization = get(id);
        final List<Amenity> amenities = amenityService.findByOrganization(organization.id());
        if (!amenities.isEmpty()) {
            return WebUtils.getMessage("organization.amenity.organization.referenced",
                    Arrays.toString(amenities.stream().map(Amenity::id).toArray())
            );
        }
        final List<User> users = userService.findByOrganization(organization.id());
        if (!users.isEmpty()) {
            return WebUtils.getMessage("organization.user.organization.referenced",
                    Arrays.toString(users.stream().map(User::id).toArray())
            );
        }
        return null;
    }
}