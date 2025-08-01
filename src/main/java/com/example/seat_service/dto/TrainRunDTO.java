package com.example.seat_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class TrainRunDTO {
    private UUID id;                // Primary key of train run
    private String trainNumber;
    private int totalSeats;
    private LocalDate departureDate;
    // Add more fields if needed, but these are enough for initialization
}
