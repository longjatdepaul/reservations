package edu.depaul.reservations.controller;

import edu.depaul.reservations.model.Amenity;
import edu.depaul.reservations.model.User;
import edu.depaul.reservations.model.Reservation;
import edu.depaul.reservations.repos.AmenityRepository;
import edu.depaul.reservations.service.ReservationService;
import edu.depaul.reservations.service.UserServiceAPI;
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
@RequestMapping("/obs-reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserServiceAPI userService;
    private final AmenityRepository amenityRepository;

    public ReservationController(final ReservationService reservationService,
                                 final UserServiceAPI userService, final AmenityRepository amenityRepository) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.amenityRepository = amenityRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userService.findAll()
                .stream()
                .collect(CustomCollectors.toSortedMap(User::username, User::fullName)));
        model.addAttribute("amenitiesValues", amenityRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Amenity::getId, Amenity::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("reservations", reservationService.findAll());
        return "reservation/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("reservation") final Reservation reservation) {
        return "reservation/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("reservation") @Valid final Reservation reservation,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reservation/add";
        }
        reservationService.create(reservation);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reservation.create.success"));
        return "redirect:/obs-reservations";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("reservation", reservationService.get(id));
        return "reservation/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("reservation") @Valid final Reservation reservation,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reservation/edit";
        }
        reservationService.update(id, reservation);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reservation.update.success"));
        return "redirect:/obs-reservations";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        reservationService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("reservation.delete.success"));
        return "redirect:/obs-reservations";
    }

}
