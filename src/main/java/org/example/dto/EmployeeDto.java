package org.example.dto;
import lombok.*;
import org.example.entity.Qualification;
import java.math.BigDecimal;

@Getter @Setter @AllArgsConstructor @ToString
public class EmployeeDto {
    private String name;
    private Qualification qualification;
    private BigDecimal salary;
    private String companyName;
}