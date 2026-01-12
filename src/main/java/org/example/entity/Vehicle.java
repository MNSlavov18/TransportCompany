package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicle")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString(callSuper = true)
public class Vehicle extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @Column(name = "license_plate", unique = true)
    private String licensePlate;

    private Double capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @ToString.Exclude
    private Company company;

    @ToString.Include(name = "companyName")
    public String getCompanyName() {
        return company != null ? company.getName() : "N/A";
    }

    // Умно изписване на капацитета
    @ToString.Include(name = "capacityInfo")
    public String getCapacityInfo() {
        if (capacity == null) return "N/A";
        if (type == VehicleType.BUS) {
            return capacity.intValue() + " места";
        } else {
            return capacity + " кг";
        }
    }
}