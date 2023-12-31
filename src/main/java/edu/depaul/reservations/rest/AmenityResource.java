package edu.depaul.reservations.rest;

import edu.depaul.reservations.model.AmenityDTO;
import edu.depaul.reservations.service.AmenityService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/amenities", produces = MediaType.APPLICATION_JSON_VALUE)
public class AmenityResource {

    private final AmenityService amenityService;

    public AmenityResource(final AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @GetMapping
    public ResponseEntity<List<AmenityDTO>> getAllAmenities() {
        return ResponseEntity.ok(amenityService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenityDTO> getAmenity(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(amenityService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAmenity(@RequestBody @Valid final AmenityDTO amenityDTO) {
        final Long createdId = amenityService.create(amenityDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAmenity(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AmenityDTO amenityDTO) {
        amenityService.update(id, amenityDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAmenity(@PathVariable(name = "id") final Long id) {
        amenityService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
