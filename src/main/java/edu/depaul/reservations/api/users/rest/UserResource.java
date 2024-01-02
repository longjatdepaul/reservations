package edu.depaul.reservations.api.users.rest;

import edu.depaul.reservations.api.users.model.User;
import edu.depaul.reservations.api.users.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


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
    public ResponseEntity<String> createUser(@RequestBody @Valid final User user) {
        final String username = userService.create(user);
        return new ResponseEntity<>(username, HttpStatus.CREATED);
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
