package edu.depaul.reservations.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Amenity {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(nullable = false, unique = true, length = 256)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(length = 256)
    private String location;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AmenityType type;

    @Column(nullable = false)
    private Integer capacity;

    @Column()
    private Double rate;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(
            name = "DaysAvailable",
            joinColumns = @JoinColumn(name = "amenityId")
    )
    private Set<DayOfWeekType> daysAvailable;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    public String toString() {
        return name;
    }
}
