package edu.depaul.reservations;

import edu.depaul.reservations.model.*;
import edu.depaul.reservations.api.addresses.repos.AddressRepository;
import edu.depaul.reservations.repos.AmenityRepository;
import edu.depaul.reservations.repos.CapacityRepository;
import edu.depaul.reservations.repos.ReservationRepository;
import edu.depaul.reservations.service.UserServiceAPI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;


@SpringBootApplication
@EntityScan("edu.depaul.reservations.model")
public class ReservationsApplication {


    private final Map<AmenityType, Integer> initialCapacities =
            new HashMap<>() {
                {
                    put(AmenityType.GYM, 20);
                    put(AmenityType.POOL, 4);
                    put(AmenityType.SAUNA, 1);
                }
            };

    public static void main(final String[] args) {
        SpringApplication.run(ReservationsApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(UserServiceAPI userService,
                                      CapacityRepository capacityRepository,
                                      AddressRepository addressRepository,
                                      AmenityRepository amenityRepository,
                                      ReservationRepository reservationRepository) {
        return (args) -> {
            User user = new User(
                    "Jonathan Lee Long",
                    "jleelong",
                    UserType.ADMINISTRATOR,
                    bCryptPasswordEncoder().encode("sEcReT")
            );
            String username = userService.create(user);

//            for (AmenityType amenityType : initialCapacities.keySet()) {
//                capacityRepository.save(new Capacity(amenityType, initialCapacities.get(amenityType)));
//            }
//
//            Address address = Address.builder()
//                    .name("Depaul Catholic High School")
//                    .street("1512 Alps Rd")
//                    .city("Wayne")
//                    .state(StateType.NEW_JERSEY)
//                    .zip("07470")
//                    .build();
//            address = addressRepository.save(address);
//
//            DayOfWeekType[] availability = new DayOfWeekType[] {
//                    DayOfWeekType.SATURDAY,
//                    DayOfWeekType.SUNDAY
//            };
//            Amenity amenity = Amenity.builder()
//                    .name("DePaul Basketball Court")
//                    .address(address)
//                    .location("Gym")
//                    .type(AmenityType.GYM)
//                    .capacity(200)
//                    .rate(99.99)
//                    .daysAvailable(new HashSet<>(Arrays.asList(availability)))
//                    .build();
//            amenity = amenityRepository.save(amenity);
//
//            Date date = new Date();
//            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            Reservation reservation = Reservation.builder()
//                    .reservationDate(localDate)
//                    .startTime(LocalTime.of(12, 0))
//                    .endTime(LocalTime.of(13, 0))
//                    .user(user)
//                    .amenities(Collections.singleton(amenity))
//                    .amenityType(AmenityType.GYM)
//                    .build();
//            reservationRepository.save(reservation);
        };
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }
}
