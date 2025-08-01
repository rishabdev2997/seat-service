package com.example.seat_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "seats", uniqueConstraints = @UniqueConstraint(columnNames = {"trainId", "departureDate", "seatNumber"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID trainId; // UUID from train-service

    @Column(nullable = false)
    private LocalDate departureDate;

    @Column(nullable = false)
    private String seatNumber; // Could be "1" to "totalSeats" as string

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status; // AVAILABLE, BOOKED
}
