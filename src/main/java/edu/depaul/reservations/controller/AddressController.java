package edu.depaul.reservations.controller;

import edu.depaul.reservations.model.Address;
import edu.depaul.reservations.model.StateType;
import edu.depaul.reservations.service.AddressService;
import edu.depaul.reservations.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(final AddressService addressService) {
        this.addressService = addressService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("stateValues", StateType.values());
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("addresses", addressService.findAll());
        return "address/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("address") final Address address) {
        return "address/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("address") @Valid final Address address,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") && addressService.nameExists(address.getName())) {
            bindingResult.rejectValue("name", "Exists.address.name");
        }
        if (bindingResult.hasErrors()) {
            return "address/add";
        }
        addressService.create(address);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("address.create.success"));
        return "redirect:/addresses";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("address", addressService.get(id));
        return "address/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("address") @Valid final Address address, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        final Address currentAddress = addressService.get(id);
        if (!bindingResult.hasFieldErrors("name") &&
                !address.getName().equalsIgnoreCase(currentAddress.getName()) &&
                addressService.nameExists(address.getName())) {
            bindingResult.rejectValue("name", "Exists.address.name");
        }
        if (bindingResult.hasErrors()) {
            return "address/edit";
        }
        addressService.update(id, address);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("address.update.success"));
        return "redirect:/addresses";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = addressService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            addressService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("address.delete.success"));
        }
        return "redirect:/addresses";
    }

}
