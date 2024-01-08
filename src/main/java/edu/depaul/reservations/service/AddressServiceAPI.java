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
public class AddressServiceAPI {

    private final String addressEndpoint;
    private final RestTemplate restTemplate;
    private final AmenityServiceAPI amenityService;
    private final OrganizationServiceAPI organizationService;
    private final UserServiceAPI userService;

    public AddressServiceAPI(final @Value("${service.endpoint.addresses}") String addressEndpoint,
                             final RestTemplate restTemplate,
                             final AmenityServiceAPI amenityService,
                             final OrganizationServiceAPI organizationService,
                             final UserServiceAPI userService) {
        this.addressEndpoint = addressEndpoint;
        this.restTemplate = restTemplate;
        this.amenityService = amenityService;
        this.organizationService = organizationService;
        this.userService = userService;
    }

    public List<Address> findAll() {
        Address[] addresses = restTemplate.getForObject(addressEndpoint, Address[].class);
        if (addresses == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(addresses);
        }
    }

    public Address get(final Long id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());
        return restTemplate.getForObject(
                addressEndpoint + "/{id}",
                Address.class,
                map
        );
    }

    public Long create(final Address address) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new Gson().toJson(address);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        return restTemplate.postForObject(addressEndpoint, request, Long.class);
    }

    public void update(final Long id, final Address address) {
        // check that address exists, will throw NotFoundException
        get(id);
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new Gson().toJson(address);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        restTemplate.put(addressEndpoint + "/{id}", request, map);
    }

    public void delete(final Long id) {
        // check that address exists, will throw NotFoundException
        get(id);
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());

        restTemplate.delete(addressEndpoint + "/{id}", map);
    }

    public boolean nameExists(final String name) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        return Boolean.TRUE.equals(restTemplate.getForObject(
                addressEndpoint + "/exists?name={name}",
                Boolean.class,
                map
        ));
    }

    public String getReferencedWarning(final Long id) {
        // check that address exists, will throw NotFoundException
        final Address address = get(id);
        final List<Amenity> amenities = amenityService.findByAddress(address.id());
        if (!amenities.isEmpty()) {
            return WebUtils.getMessage("address.amenity.address.referenced",
                    Arrays.toString(amenities.stream().map(Amenity::id).toArray())
            );
        }
        final List<Organization> organizations = organizationService.findByAddress(address.id());
        if (!organizations.isEmpty()) {
            return WebUtils.getMessage("address.organization.address.referenced",
                    Arrays.toString(organizations.stream().map(Organization::id).toArray())
            );
        }
        final List<User> users = userService.findByAddress(address.id());
        if (!users.isEmpty()) {
            return WebUtils.getMessage("address.user.address.referenced",
                    Arrays.toString(users.stream().map(User::id).toArray())
            );
        }
        return null;
    }
}
