package edu.depaul.reservations.controller;

import edu.depaul.reservations.model.Address;
import edu.depaul.reservations.service.AddressServiceAPI;
import edu.depaul.reservations.util.WebUtils;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/addresses")
public class AddressController {

    private final String stateEndpoint;
    private final String addressTypeEndpoint;
    private final AddressServiceAPI addressService;

    public AddressController(final @Value("${service.endpoint.states}") String stateEndpoint,
                             final @Value("${service.endpoint.addressTypes}") String addressTypeEndpoint,
                             final AddressServiceAPI addressService) {
        this.stateEndpoint = stateEndpoint;
        this.addressTypeEndpoint = addressTypeEndpoint;
        this.addressService = addressService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("stateEndpoint", stateEndpoint);
        model.addAttribute("addressTypeEndpoint", addressTypeEndpoint);
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
        if (!bindingResult.hasFieldErrors("name") && addressService.nameExists(address.name())) {
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
                !address.name().equalsIgnoreCase(currentAddress.name()) &&
                addressService.nameExists(address.name())) {
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
