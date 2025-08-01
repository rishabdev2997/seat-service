
package com.example.seat_service.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class UpdateSeatRequestDTO {
    private UUID trainId;
    private LocalDate departureDate;
    private String seatNumber;
    private String status; // "AVAILABLE" or "BOOKED"
}
