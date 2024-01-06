package edu.depaul.reservations.controller;

import edu.depaul.reservations.model.Address;
import edu.depaul.reservations.model.Organization;
import edu.depaul.reservations.model.User;
import edu.depaul.reservations.model.UserType;
import edu.depaul.reservations.service.AddressServiceAPI;
import edu.depaul.reservations.service.OrganizationServiceAPI;
import edu.depaul.reservations.service.UserServiceAPI;
import edu.depaul.reservations.service.UserTypeServiceAPI;
import edu.depaul.reservations.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/users")
public class UserController {

    private final String addressEndpoint;
    private final String userTypeEndpoint;
    private final String organizationEndpoint;
    private final UserServiceAPI userService;
    private final UserTypeServiceAPI userTypeService;
    private final AddressServiceAPI addressService;
    private final OrganizationServiceAPI organizationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(final @Value("${service.endpoint.addresses}") String addressEndpoint,
                          final @Value("${service.endpoint.userTypes}") String userTypeEndpoint,
                          final @Value("${service.endpoint.organizations}") String organizationEndpoint,
                          final UserServiceAPI userService,
                          final UserTypeServiceAPI userTypeService,
                          final AddressServiceAPI addressService,
                          final OrganizationServiceAPI organizationService,
                          final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.addressEndpoint = addressEndpoint;
        this.userTypeEndpoint = userTypeEndpoint;
        this.organizationEndpoint = organizationEndpoint;
        this.userService = userService;
        this.userTypeService = userTypeService;
        this.addressService = addressService;
        this.organizationService = organizationService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userTypeEndpoint", userTypeEndpoint);
        model.addAttribute("addressEndpoint", addressEndpoint);
        model.addAttribute("organizationEndpoint", organizationEndpoint);
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("user") final User user) {
        return "user/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("user") @Valid final User user,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("fullName") && userService.fullNameExists(user.fullName())) {
            bindingResult.rejectValue("fullName", "Exists.user.fullName");
        }
        if (!bindingResult.hasFieldErrors("username") && userService.usernameExists(user.username())) {
            bindingResult.rejectValue("username", "Exists.user.username");
        }
        if (bindingResult.hasErrors()) {
            return "user/add";
        }
        UserType userType = userTypeService.get(user.typeId());
        Organization organization = null;
        if (user.organizationId() != null) {
            organization = organizationService.get(user.organizationId());
        }
        User encoded = new User(
                user.id(),
                user.fullName(),
                user.username(),
                userType,
                bCryptPasswordEncoder.encode(user.passwordHash()),
                user.email(),
                user.mobile(),
                user.addressId(),
                organization,
                user.typeId(),
                user.organizationId()
        );
        userService.create(encoded);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.create.success"));
        return "redirect:/users";
    }

    @GetMapping("/edit/{username}")
    public String edit(@PathVariable(name = "username") final String username, final Model model) {
        User current = userService.get(username);
        User editing = new User(
                current.id(),
                current.fullName(),
                current.username(),
                current.type(),
                "",
                current.email(),
                current.mobile(),
                current.addressId(),
                current.organization(),
                current.typeId(),
                current.organizationId()
        );
        model.addAttribute("user", editing);
        if (current.addressId() != null) {
            Address currentAddress = addressService.get(current.addressId());
            model.addAttribute("currentAddress", String.format("%s, %s, %s, %s %s",
                    currentAddress.name(),
                    currentAddress.street(),
                    currentAddress.city(),
                    currentAddress.state(),
                    currentAddress.zip())
            );
        }
        return "user/edit";
    }

    @PostMapping("/edit/{username}")
    public String edit(@PathVariable(name = "username") final String username,
            @ModelAttribute("user") @Valid final User user, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        final User currentUser = userService.get(username);
        if (!bindingResult.hasFieldErrors("fullName") &&
                !user.fullName().equalsIgnoreCase(currentUser.fullName()) &&
                userService.fullNameExists(user.fullName())) {
            bindingResult.rejectValue("fullName", "Exists.user.fullName");
        }
        if (!bindingResult.hasFieldErrors("username") &&
                !user.username().equalsIgnoreCase(currentUser.username()) &&
                userService.usernameExists(user.username())) {
            bindingResult.rejectValue("username", "Exists.user.username");
        }
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        UserType userType = userTypeService.get(user.typeId());
        Organization organization = null;
        if (user.organizationId() != null) {
            organization = organizationService.get(user.organizationId());
        }
        User encoded = new User(
                user.id(),
                user.fullName(),
                user.username(),
                userType,
                bCryptPasswordEncoder.encode(user.passwordHash()),
                user.email(),
                user.mobile(),
                user.addressId(),
                organization,
                user.typeId(),
                user.organizationId()
        );
        userService.update(username, encoded);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.update.success"));
        return "redirect:/users";
    }

    @PostMapping("/delete/{username}")
    public String delete(@PathVariable(name = "username") final String username,
            final RedirectAttributes redirectAttributes) {
//        final String referencedWarning = userService.getReferencedWarning(id);
//        if (referencedWarning != null) {
//            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
//        } else {
            userService.delete(username);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("user.delete.success"));
//        }
        return "redirect:/users";
    }
}
