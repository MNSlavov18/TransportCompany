package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity @Table(name = "client")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString(callSuper = true)
public class Client extends BaseEntity {
    @Column(nullable = false) @NotBlank
    private String name;

    @Column(name = "has_paid_obligations")
    private boolean hasPaidObligations = false;
}