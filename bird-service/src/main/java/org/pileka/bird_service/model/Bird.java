package org.pileka.bird_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "birds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bird {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String commonName;

    @Column(nullable = false, unique = true)
    private String scientificName;

    private String family;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConservationStatus conservationStatus;

    private String typicalHabitat;

    @Column(length = 1000)
    private String description;

    private String imageUrl;

    private Double averageWingspan; // in cm

    private Double averageWeight; // in grams

    private String diet;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;

        if (o instanceof Bird that) {
            return this.id != null && this.id.equals(that.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}