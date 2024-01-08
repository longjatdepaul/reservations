package edu.depaul.reservations.api.amenities.rest;

import edu.depaul.reservations.api.amenities.model.AmenityType;
import edu.depaul.reservations.api.amenities.model.AmenityTypeItem;
import edu.depaul.reservations.api.amenities.service.AmenityTypeService;
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
@RequestMapping(value = "/api/amenitytypes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AmenityTypeResource {

    private final AmenityTypeService amenityTypeService;

    public AmenityTypeResource(final AmenityTypeService amenityTypeService) {
        this.amenityTypeService = amenityTypeService;
    }

    @GetMapping
    public ResponseEntity<List<AmenityType>> getAllAmenityTypes() {
        return ResponseEntity.ok(amenityTypeService.findAll());
    }

    @GetMapping("/search")
    public List<AmenityTypeItem> amenityTypeItems(@RequestParam(value = "q", required = false) String query) {
        if (!StringUtils.hasLength(query)) {
            return amenityTypeService.findAll().stream()
                    .map(this::mapToTypeItem)
                    .collect(Collectors.toList());
        }

        return amenityTypeService.search(query.toLowerCase()).stream()
                .limit(15)
                .map(this::mapToTypeItem)
                .collect(Collectors.toList());
    }

    private AmenityTypeItem mapToTypeItem(AmenityType type) {
        return AmenityTypeItem.builder()
                .id(type.getId())
                .text(type.getName())
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenityType> getAmenityType(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(amenityTypeService.get(id));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> getExists(
            @RequestParam(name = "name", required = false) final String name
    ) {
        if (name != null && amenityTypeService.nameExists(name)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<AmenityType> createAmenityType(@RequestBody @Valid final AmenityType amenityType) {
        final AmenityType created = amenityTypeService.create(amenityType);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAmenityType(@PathVariable(name = "id") final Long id,
                                                  @RequestBody @Valid final AmenityType amenityType) {
        amenityTypeService.update(id, amenityType);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAmenityType(@PathVariable(name = "id") final Long id) {
        amenityTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}