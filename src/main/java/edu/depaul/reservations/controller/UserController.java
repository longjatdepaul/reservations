package edu.depaul.reservations.controller;

import edu.depaul.reservations.model.UserType;
import edu.depaul.reservations.model.User;
import edu.depaul.reservations.service.UserServiceAPI;
import edu.depaul.reservations.util.WebUtils;
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

    private final UserServiceAPI userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(final UserServiceAPI userService,
                          final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("typeValues", UserType.values());
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
        User encoded = new User(
                user.fullName(),
                user.username(),
                user.type(),
                bCryptPasswordEncoder.encode(user.passwordHash())
        );
        userService.create(encoded);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.create.success"));
        return "redirect:/users";
    }

    @GetMapping("/edit/{username}")
    public String edit(@PathVariable(name = "username") final String username, final Model model) {
        User current = userService.get(username);
        User editing = new User(
                current.fullName(),
                current.username(),
                current.type(),
                ""
        );
        model.addAttribute("user", editing);
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
        User encoded = new User(
                user.fullName(),
                user.username(),
                user.type(),
                bCryptPasswordEncoder.encode(user.passwordHash())
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
