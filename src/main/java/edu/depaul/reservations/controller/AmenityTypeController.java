package edu.depaul.reservations.controller;

import edu.depaul.reservations.model.AmenityType;
import edu.depaul.reservations.service.AmenityTypeServiceAPI;
import edu.depaul.reservations.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/amenitytypes")
public class AmenityTypeController {

    private final AmenityTypeServiceAPI amenityTypeService;

    public AmenityTypeController(final AmenityTypeServiceAPI amenityTypeService) {
        this.amenityTypeService = amenityTypeService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("amenityTypes", amenityTypeService.findAll());
        return "amenitytypes/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("amenityType") final AmenityType amenityType) {
        return "amenitytypes/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("amenityType") @Valid final AmenityType amenityType,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") && amenityTypeService.nameExists(amenityType.name())) {
            bindingResult.rejectValue("name", "Exists.amenityType.name");
        }
        if (bindingResult.hasErrors()) {
            return "amenitytypes/add";
        }
        amenityTypeService.create(amenityType);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("amenityType.create.success"));
        return "redirect:/amenitytypes";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("amenityType", amenityTypeService.get(id));
        return "amenitytypes/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("amenityType") @Valid final AmenityType amenityType, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        final AmenityType currentAmenityType = amenityTypeService.get(id);
        if (!bindingResult.hasFieldErrors("name") &&
                !amenityType.name().equalsIgnoreCase(currentAmenityType.name()) &&
                amenityTypeService.nameExists(amenityType.name())) {
            bindingResult.rejectValue("name", "Exists.amenityType.name");
        }
        if (bindingResult.hasErrors()) {
            return "amenitytypes/edit";
        }
        amenityTypeService.update(id, amenityType);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("amenityType.update.success"));
        return "redirect:/amenitytypes";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = amenityTypeService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            amenityTypeService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("amenityType.delete.success"));
        }
        return "redirect:/amenitytypes";
    }
}