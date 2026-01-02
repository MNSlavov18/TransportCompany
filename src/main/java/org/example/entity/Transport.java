package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transport")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString(callSuper = true)
public class Transport extends BaseEntity {

    @Column(name = "start_point")
    @NotBlank
    private String startPoint;

    @Column(name = "end_point")
    @NotBlank
    private String endPoint;

    private LocalDate departureDate;
    private LocalDate arrivalDate;

    @Column(nullable = false)
    @Positive
    private BigDecimal price;

    private String cargoType; // Какво се превозва
    private Double cargoWeight;

    @Column(name = "is_paid")
    private boolean isPaid; // Т.6

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Employee driver;
}