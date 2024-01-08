package edu.depaul.reservations.service;

import com.google.gson.Gson;
import edu.depaul.reservations.model.*;
import edu.depaul.reservations.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class AmenityTypeServiceAPI {

    private final String amenityTypeEndpoint;
    private final RestTemplate restTemplate;
    private final AmenityServiceAPI amenityService;

    public AmenityTypeServiceAPI(final @Value("${service.endpoint.amenityTypes}") String amenityTypeEndpoint,
                                 final RestTemplate restTemplate,
                                 final AmenityServiceAPI amenityService) {
        this.amenityTypeEndpoint = amenityTypeEndpoint;
        this.restTemplate = restTemplate;
        this.amenityService = amenityService;
    }

    public List<AmenityType> findAll() {
        AmenityType[] amenityTypes = restTemplate.getForObject(amenityTypeEndpoint, AmenityType[].class);
        if (amenityTypes == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(amenityTypes);
        }
    }

    public AmenityType get(final Long id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());
        return restTemplate.getForObject(
                amenityTypeEndpoint + "/{id}",
                AmenityType.class,
                map
        );
    }

    public AmenityType create(final AmenityType amenityType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new Gson().toJson(amenityType);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        return restTemplate.postForObject(amenityTypeEndpoint, request, AmenityType.class);
    }

    public void update(final Long id, final AmenityType amenityType) {
        // check that amenity type exists, will throw NotFoundException
        get(id);
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new Gson().toJson(amenityType);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        restTemplate.put(amenityTypeEndpoint + "/{id}", request, map);
    }

    public void delete(final Long id) {
        // check that amenity type exists, will throw NotFoundException
        get(id);
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());

        restTemplate.delete(amenityTypeEndpoint + "/{id}", map);
    }

    public boolean nameExists(final String name) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        return Boolean.TRUE.equals(restTemplate.getForObject(
                amenityTypeEndpoint + "/exists?name={name}",
                Boolean.class,
                map
        ));
    }

    public String getReferencedWarning(final Long id) {
        // check that amenity type exists, will throw NotFoundException
        final AmenityType amenityType = get(id);
        final List<Amenity> amenities = amenityService.findByAmenityType(amenityType.id());
        if (!amenities.isEmpty()) {
            return WebUtils.getMessage("amenityType.amenity.amenityType.referenced",
                    Arrays.toString(amenities.stream().map(Amenity::id).toArray())
            );
        }
        return null;
    }
}