package edu.depaul.reservations.api.users.rest;

import edu.depaul.reservations.api.users.model.UserTypeItem;
import edu.depaul.reservations.api.users.model.UserType;
import edu.depaul.reservations.api.users.service.UserTypeService;
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
@RequestMapping(value = "/api/usertypes", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserTypeResource {

    private final UserTypeService userTypeService;

    public UserTypeResource(final UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    @GetMapping
    public ResponseEntity<List<UserType>> getAllUserTypes() {
        return ResponseEntity.ok(userTypeService.findAll());
    }

    @GetMapping("/search")
    public List<UserTypeItem> userTypeItems(@RequestParam(value = "q", required = false) String query) {
        if (!StringUtils.hasLength(query)) {
            return userTypeService.findAll().stream()
                    .map(this::mapToTypeItem)
                    .collect(Collectors.toList());
        }

        return userTypeService.search(query.toLowerCase()).stream()
                .limit(15)
                .map(this::mapToTypeItem)
                .collect(Collectors.toList());
    }

    private UserTypeItem mapToTypeItem(UserType type) {
        return UserTypeItem.builder()
                .id(type.getId())
                .text(type.getName())
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserType> getUserType(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(userTypeService.get(id));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> getExists(
            @RequestParam(name = "name", required = false) final String name
    ) {
        if (name != null && userTypeService.nameExists(name)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UserType> createUserType(@RequestBody @Valid final UserType userType) {
        final UserType created = userTypeService.create(userType);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateUserType(@PathVariable(name = "id") final Long id,
                                               @RequestBody @Valid final UserType userType) {
        userTypeService.update(id, userType);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUserType(@PathVariable(name = "id") final Long id) {
        userTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
