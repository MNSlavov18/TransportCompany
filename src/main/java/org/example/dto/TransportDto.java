package org.example.dto;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @ToString
public class TransportDto {
    private String id;
    private String startPoint;
    private String endPoint;
    private BigDecimal price;
    private String driverName;
    private String companyName;
    private LocalDate departureDate;
}