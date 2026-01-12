package org.example.dto;
import lombok.*;

@Getter @Setter @AllArgsConstructor @ToString
public class ClientDto {
    private String name;
    private String paymentStatus;
}