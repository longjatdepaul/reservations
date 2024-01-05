package edu.depaul.reservations.api.users.rest;

import edu.depaul.reservations.api.users.model.Organization;
import edu.depaul.reservations.api.users.model.OrganizationItem;
import edu.depaul.reservations.api.users.service.OrganizationService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/organizations", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationResource {

    private final OrganizationService organizationService;

    public OrganizationResource(final OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.findAll());
    }

    @GetMapping("/search")
    public List<OrganizationItem> organizationItems(@RequestParam(value = "q", required = false) String query) {
        if (!StringUtils.hasLength(query)) {
            return organizationService.findAll().stream()
                    .map(this::mapToOrganizationItem)
                    .collect(Collectors.toList());
        }

        return organizationService.search(query.toLowerCase()).stream()
                .limit(15)
                .map(this::mapToOrganizationItem)
                .collect(Collectors.toList());
    }

    private OrganizationItem mapToOrganizationItem(Organization organization) {
        return OrganizationItem.builder()
                .id(organization.getId())
                .text(organization.getName())
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganization(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(organizationService.get(id));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> getExists(
            @RequestParam(name = "name", required = false) final String name
    ) {
        if (name != null && organizationService.nameExists(name)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Organization> createOrganization(@RequestBody @Valid final Organization organization) {
        final Organization created = organizationService.create(organization);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateOrganizatione(@PathVariable(name = "id") final Long id,
                                                    @RequestBody @Valid final Organization organization) {
        organizationService.update(id, organization);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOrganization(@PathVariable(name = "id") final Long id) {
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
