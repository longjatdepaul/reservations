package edu.depaul.reservations.controller;

import edu.depaul.reservations.model.Address;
import edu.depaul.reservations.model.Organization;
import edu.depaul.reservations.model.User;
import edu.depaul.reservations.service.AddressServiceAPI;
import edu.depaul.reservations.service.OrganizationServiceAPI;
import edu.depaul.reservations.service.UserServiceAPI;
import edu.depaul.reservations.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/organizations")
public class OrganizationController {

    private final String userEndpoint;
    private final String addressEndpoint;
    private final OrganizationServiceAPI organizationService;
    private final UserServiceAPI userService;
    private final AddressServiceAPI addressService;

    public OrganizationController(final @Value("${service.endpoint.users}") String userEndpoint,
                                  final @Value("${service.endpoint.addresses}") String addressEndpoint,
                                  final OrganizationServiceAPI organizationService,
                                  final UserServiceAPI userService,
                                  final AddressServiceAPI addressService) {
        this.userEndpoint = userEndpoint;
        this.addressEndpoint = addressEndpoint;
        this.organizationService = organizationService;
        this.userService = userService;
        this.addressService = addressService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userEndpoint", userEndpoint);
        model.addAttribute("addressEndpoint", addressEndpoint);
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("organizations", organizationService.findAll());
        return "organization/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("organization") final Organization organization) {
        return "organization/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("organization") @Valid final Organization organization,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") && organizationService.nameExists(organization.name())) {
            bindingResult.rejectValue("name", "Exists.organization.name");
        }
        if (bindingResult.hasErrors()) {
            return "organization/add";
        }
        organizationService.create(organization);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("organization.create.success"));
        return "redirect:/organizations";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        Organization current = organizationService.get(id);
        model.addAttribute("organization", current);
        if (current.contactUser() != null) {
            User currentContact = userService.get(current.contactUser());
            model.addAttribute("currentContact", currentContact.fullName());
        }
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
        return "organization/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("organization") @Valid final Organization organization, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        final Organization currentOrganization = organizationService.get(id);
        if (!bindingResult.hasFieldErrors("name") &&
                !organization.name().equalsIgnoreCase(currentOrganization.name()) &&
                organizationService.nameExists(organization.name())) {
            bindingResult.rejectValue("name", "Exists.organization.name");
        }
        if (bindingResult.hasErrors()) {
            return "organization/edit";
        }
        organizationService.update(id, organization);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("organization.update.success"));
        return "redirect:/organizations";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        organizationService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("organization.delete.success"));
        return "redirect:/organizations";
    }
}
