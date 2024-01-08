package edu.depaul.reservations.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.depaul.reservations.model.Amenity;
import edu.depaul.reservations.util.GsonLocalTimeAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional
public class AmenityServiceAPI {

    private final String amenityEndpoint;
    private final RestTemplate restTemplate;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalTime.class, new GsonLocalTimeAdapter()).create();

    public AmenityServiceAPI(final @Value("${service.endpoint.amenities}") String amenityEndpoint,
                             final RestTemplate restTemplate) {
        this.amenityEndpoint = amenityEndpoint;
        this.restTemplate = restTemplate;
    }

    public List<Amenity> findAll() {
        Amenity[] amenities = restTemplate.getForObject(amenityEndpoint, Amenity[].class);
        if (amenities == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(amenities);
        }
    }

    public List<Amenity> findByAddress(final Long addressId) {
        Map<String, String> map = new HashMap<>();
        map.put("addressId", addressId.toString());
        Amenity[] amenities = restTemplate.getForObject(
                amenityEndpoint + "/at/{addressId}",
                Amenity[].class,
                map
        );
        if (amenities == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(amenities);
        }
    }

    public List<Amenity> findByAmenityType(final Long typeId) {
        Map<String, String> map = new HashMap<>();
        map.put("typeId", typeId.toString());
        Amenity[] amenities = restTemplate.getForObject(
                amenityEndpoint + "/of/{typeId}",
                Amenity[].class,
                map
        );
        if (amenities == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(amenities);
        }
    }

    public List<Amenity> findByOrganization(final Long organizationId) {
        Map<String, String> map = new HashMap<>();
        map.put("organizationId", organizationId.toString());
        Amenity[] amenities = restTemplate.getForObject(
                amenityEndpoint + "/in/{organizationId}",
                Amenity[].class,
                map
        );
        if (amenities == null) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(amenities);
        }
    }

    public Amenity get(final Long id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());
        return restTemplate.getForObject(
                amenityEndpoint + "/{id}",
                Amenity.class,
                map
        );
    }

    public Long create(final Amenity amenity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = gson.toJson(amenity);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        return restTemplate.postForObject(amenityEndpoint, request, Long.class);
    }

    public void update(final Long id, final Amenity amenity) {
        // check that amenity exists, will throw NotFoundException
        get(id);
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = gson.toJson(amenity);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        restTemplate.put(amenityEndpoint + "/{id}", request, map);
    }

    public void delete(final Long id) {
        // check that amenity exists, will throw NotFoundException
        get(id);
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());

        restTemplate.delete(amenityEndpoint + "/{id}", map);
    }

    public boolean nameExists(final String name) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        return Boolean.TRUE.equals(restTemplate.getForObject(
                amenityEndpoint + "/exists?name={name}",
                Boolean.class,
                map
        ));
    }
}
