package com.example.seat_service.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class BatchUpdateSeatRequestDTO {
    private UUID trainId;
    private LocalDate departureDate;
    private List<String> seatNumbers;
    private String status; // "BOOKED" or "AVAILABLE"
}
