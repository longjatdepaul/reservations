package edu.depaul.reservations;

import edu.depaul.reservations.model.*;
import edu.depaul.reservations.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;


@SpringBootApplication
@EntityScan("edu.depaul.reservations.model")
public class ReservationsApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ReservationsApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(UserTypeServiceAPI userTypeService,
                                      AddressServiceAPI addressService,
                                      UserServiceAPI userService,
                                      OrganizationServiceAPI organizationService,
                                      AmenityTypeServiceAPI amenityTypeService,
                                      AmenityServiceAPI amenityService) {
        return (args) -> {
            UserType admin = new UserType(
                    0L,
                    "Administrator",
                    "provides permission to manage user accounts, organizations and roles"
            );
            admin = userTypeService.create(admin);

            Address address = new Address(
                    0L,
                    "Long Jersey Shore Residence",
                    "RESIDENTIAL",
                    "15 Sailors Way",
                    "Middletown",
                    "NJ",
                    "07748"
            );
            Long addressId = addressService.create(address);

            User user = new User(
                    0L,
                    "Jonathan Lee Long",
                    "longj",
                    admin,
                    bCryptPasswordEncoder().encode("secret"),
                    "longj@depaulcatholic.org",
                    "+1 (973) 694-3702",
                    addressId,
                    null,
                    admin.id(),
                    null
            );
            user = userService.create(user);

            UserType customer = new UserType(
                    0L,
                    "Customer",
                    "provides permission to manage personal addresses and create reservations"
            );
            userTypeService.create(customer);

            UserType owner = new UserType(
                    0L,
                    "Owner",
                    "provides permission to create and manage amenities"
            );
            userTypeService.create(owner);

            address = new Address(
                    0L,
                    "DePaul Catholic High School",
                    "BUSINESS",
                    "1512 Alps Rd",
                    "Wayne",
                    "NJ",
                    "07470"
            );
            addressId = addressService.create(address);

            Organization organization = new Organization(
                    0L,
                    "DePaul Catholic High School",
                    user.username(),
                    addressId
            );
            organization = organizationService.create(organization);

            user = new User(
                    user.id(),
                    user.fullName(),
                    user.username(),
                    user.type(),
                    user.passwordHash(),
                    user.email(),
                    user.mobile(),
                    user.addressId(),
                    organization,
                    admin.id(),
                    organization.id()
            );
            userService.update(user.username(), user);

            AmenityType amenityType = new AmenityType(
                    0L,
                    "Gym",
                    "an indoor venue for exercise and sports"
            );
            amenityType = amenityTypeService.create(amenityType);

            Amenity amenity = new Amenity(
                    0L,
                    "DePaul Basketball Court",
                    organization.id(),
                    addressId,
                    amenityType,
                    List.of("Main Gym"),
                    null,
                    Set.of("SATURDAY", "SUNDAY"),
                    LocalTime.of(8,0,0,0),
                    LocalTime.of(18,0,0,0),
                    30,
                    "full court gym with bleachers that sit 150",
                    amenityType.id(),
                    "Main Gym"
            );
            amenityService.create(amenity);
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
