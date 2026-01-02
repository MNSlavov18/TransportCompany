package org.example.dto;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @AllArgsConstructor @ToString
public class CompanyDto {
    private String name;
    private BigDecimal revenue;
}