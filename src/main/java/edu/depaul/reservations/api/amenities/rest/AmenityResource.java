package edu.depaul.reservations.api.amenities.rest;

import edu.depaul.reservations.api.amenities.model.Amenity;
import edu.depaul.reservations.api.amenities.model.AmenityType;
import edu.depaul.reservations.api.amenities.service.AmenityService;
import edu.depaul.reservations.api.amenities.service.AmenityTypeService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.Valid;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/amenities", produces = MediaType.APPLICATION_JSON_VALUE)
public class AmenityResource {

    private final AmenityService amenityService;
    private final AmenityTypeService amenityTypeService;

    public AmenityResource(final AmenityService amenityService,
                           final AmenityTypeService amenityTypeService) {
        this.amenityService = amenityService;
        this.amenityTypeService = amenityTypeService;
    }

    @GetMapping
    public ResponseEntity<List<Amenity>> getAllAmenities() {
        return ResponseEntity.ok(amenityService.findAll());
    }

    @GetMapping("/at/{addressId}")
    public ResponseEntity<List<Amenity>> getAmenitiesAt(@PathVariable(name = "addressId") final Long addressId) {
        return ResponseEntity.ok(amenityService.getAt(addressId));
    }

    @GetMapping("/of/{typeId}")
    public ResponseEntity<List<Amenity>> getAmenitiesOf(@PathVariable(name = "typeId") final Long typeId) {
        final AmenityType amenityType = amenityTypeService.get(typeId);
        return ResponseEntity.ok(amenityService.getOf(amenityType));
    }

    @GetMapping("/in/{organizationId}")
    public ResponseEntity<List<Amenity>> getAmenitiesIn(@PathVariable(name = "organizationId") final Long organizationId) {
        return ResponseEntity.ok(amenityService.getIn(organizationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Amenity> getAmenity(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(amenityService.get(id));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> getExists(
            @RequestParam(name = "name", required = false) final String name
    ) {
        if (name != null && amenityService.nameExists(name)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAmenity(@RequestBody @Valid final Amenity amenity) {
        final Long createdId = amenityService.create(amenity);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAmenity(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final Amenity amenity) {
        amenityService.update(id, amenity);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAmenity(@PathVariable(name = "id") final Long id) {
        amenityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
