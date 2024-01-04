package edu.depaul.reservations.api.addresses.rest;

import edu.depaul.reservations.api.addresses.model.AddressType;
import edu.depaul.reservations.api.addresses.model.AddressTypeItem;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addresstypes")
public class AddressTypeResource {

    @GetMapping
    public List<AddressTypeItem> addressTypeItems(@RequestParam(value = "q", required = false) String query) {
        if (!StringUtils.hasLength(query)) {
            return Arrays.stream(AddressType.values())
                    .limit(15)
                    .map(this::mapToAddressTypeItem)
                    .collect(Collectors.toList());
        }

        return Arrays.stream(AddressType.values())
                .filter(type -> type.getLabel()
                        .toLowerCase()
                        .contains(query.toLowerCase()))
                .limit(15)
                .map(this::mapToAddressTypeItem)
                .collect(Collectors.toList());
    }

    private AddressTypeItem mapToAddressTypeItem(AddressType type) {
        return AddressTypeItem.builder()
                .id(type)
                .text(type.getLabel())
                .slug(type.name())
                .build();
    }
}
