package edu.depaul.reservations.api.users.rest;

import edu.depaul.reservations.api.users.model.UserTypeItem;
import edu.depaul.reservations.api.users.model.UserType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usertypes")
public class UserTypeResource {

    @GetMapping
    public List<UserTypeItem> userTypeItems(@RequestParam(value = "q", required = false) String query) {
        if (!StringUtils.hasLength(query)) {
            return Arrays.stream(UserType.values())
                    .limit(15)
                    .map(this::mapToTypeItem)
                    .collect(Collectors.toList());
        }

        return Arrays.stream(UserType.values())
                .filter(type -> type.getLabel()
                        .toLowerCase()
                        .contains(query.toLowerCase()))
                .limit(15)
                .map(this::mapToTypeItem)
                .collect(Collectors.toList());
    }

    private UserTypeItem mapToTypeItem(UserType type) {
        return UserTypeItem.builder()
                .id(type)
                .text(type.getLabel())
                .slug(type.name())
                .build();
    }
}
