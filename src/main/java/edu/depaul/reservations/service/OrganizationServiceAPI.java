package edu.depaul.reservations.service;

import com.google.gson.Gson;
import edu.depaul.reservations.model.Organization;
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

    public OrganizationServiceAPI(final @Value("${service.endpoint.organizations}") String organizationEndpoint,
                                  final RestTemplate restTemplate) {
        this.organizationEndpoint = organizationEndpoint;
        this.restTemplate = restTemplate;
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
}
