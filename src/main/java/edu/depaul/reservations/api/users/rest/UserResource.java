package edu.depaul.reservations.api.users.rest;

import edu.depaul.reservations.api.addresses.model.Address;
import edu.depaul.reservations.api.addresses.model.AddressItem;
import edu.depaul.reservations.api.users.model.User;
import edu.depaul.reservations.api.users.model.UserItem;
import edu.depaul.reservations.api.users.service.UserService;
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

    public UserResource(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
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
                .id(user.getId())
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
    public ResponseEntity<Long> createUser(@RequestBody @Valid final User user) {
        final Long userId = userService.create(user);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
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
