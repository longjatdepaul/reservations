package edu.depaul.reservations.api.amenities.model;

import javax.persistence.*;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false)
    private Long organizationId;

    @Column(nullable = false)
    private Long addressId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false)
    private AmenityType type;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "resources", joinColumns = @JoinColumn(name = "amenity_id"))
    @Column(name = "resource", nullable = false, length = 256)
    private List<String> resources = new ArrayList<>();

    @Column()
    private Double rate;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "DaysAvailable", joinColumns = @JoinColumn(name = "amenity_id"))
    @Column(nullable = false)
    private Set<DayOfWeek> daysAvailable;
;
    @DateTimeFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime timeAvailableStarting;

    @DateTimeFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime timeAvailableEnding;

    @Column()
    private Integer transitionMinutes;

    @Column(length = 512)
    private String description;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @PrePersist
    public void prePersist() {
        dateCreated = OffsetDateTime.now();
        lastUpdated = dateCreated;
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdated = OffsetDateTime.now();
    }

    public String toString() {
        return name;
    }
}
