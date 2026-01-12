package org.example.dto;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @AllArgsConstructor @ToString
public class CompanyIncomeDto {
    private String companyName;
    private BigDecimal totalRevenue;
}