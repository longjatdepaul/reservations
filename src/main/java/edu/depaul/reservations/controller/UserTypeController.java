package edu.depaul.reservations.controller;

import edu.depaul.reservations.model.UserType;
import edu.depaul.reservations.service.UserTypeServiceAPI;
import edu.depaul.reservations.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/usertypes")
public class UserTypeController {

    private final UserTypeServiceAPI userTypeService;

    public UserTypeController(final UserTypeServiceAPI userTypeService) {
        this.userTypeService = userTypeService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("userTypes", userTypeService.findAll());
        return "usertypes/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("userType") final UserType userType) {
        return "usertypes/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("userType") @Valid final UserType userType,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") && userTypeService.nameExists(userType.name())) {
            bindingResult.rejectValue("name", "Exists.userType.name");
        }
        if (bindingResult.hasErrors()) {
            return "usertypes/add";
        }
        userTypeService.create(userType);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("userType.create.success"));
        return "redirect:/usertypes";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("userType", userTypeService.get(id));
        return "usertypes/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("userType") @Valid final UserType userType, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        final UserType currentUserType = userTypeService.get(id);
        if (!bindingResult.hasFieldErrors("name") &&
                !userType.name().equalsIgnoreCase(currentUserType.name()) &&
                userTypeService.nameExists(userType.name())) {
            bindingResult.rejectValue("name", "Exists.userType.name");
        }
        if (bindingResult.hasErrors()) {
            return "usertypes/edit";
        }
        userTypeService.update(id, userType);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("userType.update.success"));
        return "redirect:/usertypes";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = userTypeService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            userTypeService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("userType.delete.success"));
        }
        return "redirect:/usertypes";
    }
}