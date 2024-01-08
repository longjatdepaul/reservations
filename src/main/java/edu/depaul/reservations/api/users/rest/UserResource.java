package edu.depaul.reservations.api.users.rest;

import edu.depaul.reservations.api.users.model.Organization;
import edu.depaul.reservations.api.users.model.User;
import edu.depaul.reservations.api.users.model.UserItem;
import edu.depaul.reservations.api.users.model.UserType;
import edu.depaul.reservations.api.users.service.OrganizationService;
import edu.depaul.reservations.api.users.service.UserService;
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
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserResource {

    private final UserService userService;
    private final UserTypeService userTypeService;
    private final OrganizationService organizationService;

    public UserResource(final UserService userService,
                        final UserTypeService userTypeService,
                        final OrganizationService organizationService) {
        this.userService = userService;
        this.userTypeService = userTypeService;
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/at/{addressId}")
    public ResponseEntity<List<User>> getUsersAt(@PathVariable(name = "addressId") final Long addressId) {
        return ResponseEntity.ok(userService.getAt(addressId));
    }

    @GetMapping("/of/{typeId}")
    public ResponseEntity<List<User>> getUsersOf(@PathVariable(name = "typeId") final Long typeId) {
        final UserType userType = userTypeService.get(typeId);
        return ResponseEntity.ok(userService.getOf(userType));
    }

    @GetMapping("/in/{organizationId}")
    public ResponseEntity<List<User>> getUsersIn(@PathVariable(name = "organizationId") final Long organizationId) {
        final Organization organization = organizationService.get(organizationId);
        return ResponseEntity.ok(userService.getIn(organization));
    }

    @GetMapping("/search")
    public List<UserItem> userItems(@RequestParam(value = "q", required = false) String query) {
        if (!StringUtils.hasLength(query)) {
            return userService.findAll().stream()
                    .map(this::mapToUserItem)
                    .collect(Collectors.toList());
        }

        return userService.search(query.toLowerCase()).stream()
                .limit(15)
                .map(this::mapToUserItem)
                .collect(Collectors.toList());
    }

    private UserItem mapToUserItem(User user) {
        return UserItem.builder()
                .id(user.getUsername())
                .text(user.getFullName())
                .build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable(name = "username") final String username) {
        return ResponseEntity.ok(userService.get(username));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> getExists(
            @RequestParam(name = "username", required = false) final String username,
            @RequestParam(name = "fullName", required = false) final String fullName
    ) {
        if (username != null && userService.usernameExists(username)) {
            return ResponseEntity.ok(true);
        }
        if (fullName != null && userService.fullNameExists(fullName)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<User> createUser(@RequestBody @Valid final User user) {
        User created = userService.create(user);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable(name = "username") final String username,
            @RequestBody @Valid final User user) {
        User current = getUser(username).getBody();
        if (current != null) {
            User reference = new User(
                    current.getId(),
                    user.getFullName(),
                    user.getUsername(),
                    user.getType(),
                    user.getPasswordHash(),
                    user.getEmail(),
                    user.getMobile(),
                    user.getAddressId(),
                    user.getOrganization(),
                    current.getDateCreated(),
                    current.getLastUpdated()
            );
            userService.update(username, reference);
        }
        return ResponseEntity.ok(username);
    }

    @DeleteMapping("/{username}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "username") final String username) {
        userService.delete(username);
        return ResponseEntity.noContent().build();
    }
}
