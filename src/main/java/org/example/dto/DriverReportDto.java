package org.example.dto;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @AllArgsConstructor @ToString
public class DriverReportDto {
    private String driverName;
    private String companyName;
    private Long tripCount;
    private BigDecimal totalIncome;
}