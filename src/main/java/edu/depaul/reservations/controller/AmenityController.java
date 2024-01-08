package edu.depaul.reservations.controller;

import edu.depaul.reservations.model.*;
import edu.depaul.reservations.service.AddressServiceAPI;
import edu.depaul.reservations.service.AmenityServiceAPI;
import edu.depaul.reservations.service.AmenityTypeServiceAPI;
import edu.depaul.reservations.service.OrganizationServiceAPI;
import edu.depaul.reservations.util.WebUtils;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/amenities")
public class AmenityController {

    private final String organizationEndpoint;
    private final String addressEndpoint;
    private final String amenityTypeEndpoint;
    private final String daysOfWeekEndpoint;
    private final AmenityServiceAPI amenityService;
    private final OrganizationServiceAPI organizationService;
    private final AddressServiceAPI addressService;
    private final AmenityTypeServiceAPI amenityTypeService;

    public AmenityController(final @Value("${service.endpoint.organizations}") String organizationEndpoint,
                             final @Value("${service.endpoint.addresses}") String addressEndpoint,
                             final @Value("${service.endpoint.amenityTypes}") String amenityTypeEndpoint,
                             final @Value("${service.endpoint.daysOfWeek}") String daysOfWeekEndpoint,
                             final AmenityServiceAPI amenityService,
                             final OrganizationServiceAPI organizationService,
                             final AddressServiceAPI addressService,
                             final AmenityTypeServiceAPI amenityTypeService) {
        this.organizationEndpoint = organizationEndpoint;
        this.addressEndpoint = addressEndpoint;
        this.amenityTypeEndpoint = amenityTypeEndpoint;
        this.daysOfWeekEndpoint = daysOfWeekEndpoint;
        this.amenityService = amenityService;
        this.organizationService = organizationService;
        this.addressService = addressService;
        this.amenityTypeService = amenityTypeService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("organizationEndpoint", organizationEndpoint);
        model.addAttribute("addressEndpoint", addressEndpoint);
        model.addAttribute("amenityTypeEndpoint", amenityTypeEndpoint);
        model.addAttribute("daysOfWeekEndpoint", daysOfWeekEndpoint);
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("amenities", amenityService.findAll());
        return "amenity/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("amenity") final Amenity amenity) {
        return "amenity/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("amenity") @Valid final Amenity amenity,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") && amenityService.nameExists(amenity.name())) {
            bindingResult.rejectValue("name", "Exists.amenity.name");
        }
        if (bindingResult.hasErrors()) {
            return "amenity/add";
        }
        AmenityType amenityType = amenityTypeService.get(amenity.typeId());
        Amenity mashup = new Amenity(
                amenity.id(),
                amenity.name(),
                amenity.organizationId(),
                amenity.addressId(),
                amenityType,
                amenity.resources(),
                amenity.rate(),
                amenity.daysAvailable(),
                amenity.timeAvailableStarting(),
                amenity.timeAvailableEnding(),
                amenity.transitionMinutes(),
                amenity.description(),
                amenity.typeId(),
                amenity.resourcesString()
        );
        amenityService.create(mashup);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("amenity.create.success"));
        return "redirect:/amenities";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        Amenity current = amenityService.get(id);
        model.addAttribute("amenity", current);
        if (current.organizationId() != null) {
            Organization currentOrganization = organizationService.get(current.organizationId());
            model.addAttribute("currentOrganization", currentOrganization.name());
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
        return "amenity/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("amenity") @Valid final Amenity amenity,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final Amenity currentAmenity = amenityService.get(id);
        if (!bindingResult.hasFieldErrors("name") &&
                !amenity.name().equalsIgnoreCase(currentAmenity.name()) &&
                amenityService.nameExists(amenity.name())) {
            bindingResult.rejectValue("name", "Exists.amenity.name");
        }
        if (bindingResult.hasErrors()) {
            return "amenity/edit";
        }
        AmenityType amenityType = amenityTypeService.get(amenity.typeId());
        Amenity mashup = new Amenity(
                amenity.id(),
                amenity.name(),
                amenity.organizationId(),
                amenity.addressId(),
                amenityType,
                amenity.resources(),
                amenity.rate(),
                amenity.daysAvailable(),
                amenity.timeAvailableStarting(),
                amenity.timeAvailableEnding(),
                amenity.transitionMinutes(),
                amenity.description(),
                amenity.typeId(),
                amenity.resourcesString()
        );
        amenityService.update(id, mashup);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("amenity.update.success"));
        return "redirect:/amenities";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        amenityService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("amenity.delete.success"));
        return "redirect:/amenities";
    }
}