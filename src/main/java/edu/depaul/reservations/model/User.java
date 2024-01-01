package edu.depaul.reservations.model;

import javax.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;

import lombok.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Entity
@Table(name = "\"USER\"")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ComponentScan(basePackages = "edu.depaul.reservations.config")
public class User {

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
    private String fullName;

    @Column(nullable = false, unique = true, length = 16)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(length = 256)
    private String passwordHash;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Reservation> reservations;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @PrePersist
    public void prePersist() {
        passwordHash = new BCryptPasswordEncoder().encode(passwordHash);
        dateCreated = OffsetDateTime.now();
        lastUpdated = dateCreated;
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdated = OffsetDateTime.now();
    }

    public String toString() {
        return fullName;
    }

    public User(String fullName, String username, String passwordHash) {
        this.fullName = fullName;
        this.username = username;
        this.passwordHash = passwordHash;
    }

}
