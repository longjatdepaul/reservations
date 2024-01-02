package edu.depaul.reservations;

import edu.depaul.reservations.model.Reservation;
import edu.depaul.reservations.model.User;
import edu.depaul.reservations.service.ReservationService;
import edu.depaul.reservations.service.UserServiceAPI;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

//import java.util.Set;


@Controller
public class PagesController {

    final UserServiceAPI userService;
    final ReservationService reservationService;

    public PagesController(UserServiceAPI userService, ReservationService reservationService) {
        this.userService = userService;
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/reservations")
    public String reservations(Model model, HttpSession session) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = principal.getUsername();
        User user = userService.get(name);

        // This should always be the case
        if (user != null) {
            session.setAttribute("user", user);

            // Empty reservation object in case the user creates a new reservation
            Reservation reservation = new Reservation();
            model.addAttribute("reservation", reservation);

            return "reservations";
        }

        return "index";
    }

    @PostMapping("/reservations-submit")
    public String reservationsSubmit(@ModelAttribute Reservation reservation,
                                     Model model, @SessionAttribute("user") User user) {
        // Save to DB after updating
        assert user != null;
        reservation.setUser(user.username());
        reservationService.create(reservation);
        //Set<Reservation> userReservations = user.getReservations();
        //userReservations.add(reservation);
        //user.setReservations(userReservations);
        //userService.update(user.getId(), user);
        return "redirect:/reservations";
    }

}
