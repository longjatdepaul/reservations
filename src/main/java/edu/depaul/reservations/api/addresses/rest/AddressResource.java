package edu.depaul.reservations.api.addresses.rest;

import edu.depaul.reservations.api.addresses.model.Address;
import edu.depaul.reservations.api.addresses.model.AddressItem;
import edu.depaul.reservations.api.addresses.model.State;
import edu.depaul.reservations.api.addresses.model.StateItem;
import edu.depaul.reservations.api.addresses.service.AddressService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressResource {

    private final AddressService addressService;

    public AddressResource(final AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
        return ResponseEntity.ok(addressService.findAll());
    }

    @GetMapping("/search")
    public List<AddressItem> addressItems(@RequestParam(value = "q", required = false) String query) {
        if (!StringUtils.hasLength(query)) {
            return addressService.findAll().stream()
                    .map(this::mapToAddressItem)
                    .collect(Collectors.toList());
        }

        return addressService.search(query.toLowerCase()).stream()
                .limit(15)
                .map(this::mapToAddressItem)
                .collect(Collectors.toList());
    }

    private AddressItem mapToAddressItem(Address address) {
        return AddressItem.builder()
                .id(address.getId())
                .text(String.format("%s, %s, %s, %s %s",
                        address.getName(),
                        address.getStreet(),
                        address.getCity(),
                        address.getState(),
                        address.getZip()))
                .slug(address.getId().toString())
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddress(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(addressService.get(id));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> getExists(
            @RequestParam(name = "name", required = false) final String name
    ) {
        if (name != null && addressService.nameExists(name)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAddress(@RequestBody @Valid final Address address) {
        final Long createdId = addressService.create(address);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAddress(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final Address address) {
        addressService.update(id, address);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAddress(@PathVariable(name = "id") final Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
