package edu.depaul.reservations.controller;

import edu.depaul.reservations.api.addresses.model.Address;
import edu.depaul.reservations.model.*;
import edu.depaul.reservations.api.addresses.repos.AddressRepository;
import edu.depaul.reservations.service.AmenityService;
import edu.depaul.reservations.util.CustomCollectors;
import edu.depaul.reservations.util.WebUtils;
import javax.validation.Valid;
import org.springframework.data.domain.Sort;
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

    private final AmenityService amenityService;
    private final AddressRepository addressRepository;

    public AmenityController(final AmenityService amenityService,
                             final AddressRepository addressRepository) {
        this.amenityService = amenityService;
        this.addressRepository = addressRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("typeValues", AmenityType.values());
        model.addAttribute("daysAvailableValues", DayOfWeekType.values());
        model.addAttribute("addressValues", addressRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Address::getId, Address::getName)));
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
        if (!bindingResult.hasFieldErrors("name") && amenityService.nameExists(amenity.getName())) {
            bindingResult.rejectValue("name", "Exists.amenity.name");
        }
        if (bindingResult.hasErrors()) {
            return "amenity/add";
        }
        amenityService.create(amenity);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("amenity.create.success"));
        return "redirect:/amenities";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("amenity", amenityService.get(id));
        return "amenity/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("amenity") @Valid final Amenity amenity,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final Amenity currentAmenity = amenityService.get(id);
        if (!bindingResult.hasFieldErrors("name") &&
                !amenity.getName().equalsIgnoreCase(currentAmenity.getName()) &&
                amenityService.nameExists(amenity.getName())) {
            bindingResult.rejectValue("name", "Exists.amenity.name");
        }
        if (bindingResult.hasErrors()) {
            return "amenity/edit";
        }
        amenityService.update(id, amenity);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("amenity.update.success"));
        return "redirect:/amenities";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = amenityService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            amenityService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("amenity.delete.success"));
        }
        return "redirect:/amenities";
    }

}
