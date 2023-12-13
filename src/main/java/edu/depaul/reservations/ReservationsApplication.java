package edu.depaul.reservations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan("edu.depaul.reservations.model")
public class ReservationsApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ReservationsApplication.class, args);
    }

}
