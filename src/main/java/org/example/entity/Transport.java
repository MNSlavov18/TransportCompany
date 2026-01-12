package org.example.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "transport")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString(callSuper = true)
public class Transport extends BaseEntity {
    @Column(name = "start_point") @NotBlank private String startPoint;
    @Column(name = "end_point") @NotBlank private String endPoint;

    private LocalDate departureDate;
    private LocalDate arrivalDate;

    @Column(nullable = false) @Positive private BigDecimal price;

    @Enumerated(EnumType.STRING) private CargoType cargoType;
    private Double cargoWeight;
    private Integer passengerCount;
    @Column(name = "is_paid") private boolean isPaid;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "company_id") @ToString.Exclude private Company company;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "client_id") @ToString.Exclude private Client client;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "driver_id") @ToString.Exclude private Employee driver;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "vehicle_id") @ToString.Exclude private Vehicle vehicle;

    @ToString.Include(name = "details")
    public String getDetails() {
        return (cargoType == CargoType.PASSENGERS) ? passengerCount + " passengers" : cargoWeight + " kg";
    }
    @ToString.Include(name = "driverName") public String getDriverName() { return driver != null ? driver.getName() : "N/A"; }
    @ToString.Include(name = "companyName") public String getCompanyName() { return company != null ? company.getName() : "N/A"; }
    @ToString.Include(name = "vehicleInfo") public String getVehicleInfo() { return vehicle != null ? vehicle.getLicensePlate() + " (" + vehicle.getType() + ")" : "N/A"; }
}