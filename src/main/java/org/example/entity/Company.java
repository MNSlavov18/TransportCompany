package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "company")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString(callSuper = true)
public class Company extends BaseEntity {

    @Column(name = "name", unique = true, nullable = false)
    @NotBlank(message = "Company name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 chars")
    private String name;

    @Column(name = "revenue")
    @PositiveOrZero
    private BigDecimal revenue = BigDecimal.ZERO;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Employee> employees;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Vehicle> vehicles;
}