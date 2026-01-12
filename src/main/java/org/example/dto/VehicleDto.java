package org.example.dto;
import lombok.*;
import org.example.entity.VehicleType;

@Getter @Setter @AllArgsConstructor @ToString
public class VehicleDto {
    private String licensePlate;
    private VehicleType type;
    private String capacityInfo;
    private String ownerCompany;
}