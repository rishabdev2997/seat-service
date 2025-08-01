package com.example.seat_service.controller;

import com.example.seat_service.model.Seat;
import com.example.seat_service.dto.UpdateSeatRequestDTO;
import com.example.seat_service.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping
    public ResponseEntity<List<Seat>> getSeats(
            @RequestParam UUID trainId,
            @RequestParam String departureDate // yyyy-MM-dd
    ) {
        LocalDate date = LocalDate.parse(departureDate);
        return ResponseEntity.ok(seatService.getSeatsForTrainOnDate(trainId, date));
    }

    @PostMapping
    public ResponseEntity<Seat> createSeat(@RequestBody Seat seat) {
        return ResponseEntity.ok(seatService.createSeat(seat));
    }

    // For initializing all seats for a train+date in one call
    @PostMapping("/initialize")
    public ResponseEntity<List<Seat>> initializeSeats(
            @RequestParam UUID trainId,
            @RequestParam String departureDate, // yyyy-MM-dd
            @RequestParam int totalSeats
    ) {
        LocalDate date = LocalDate.parse(departureDate);
        List<Seat> seats = seatService.initializeSeats(trainId, date, totalSeats);
        return ResponseEntity.ok(seats);
    }

    // ✅ NEW: Update a single seat's status (BOOKED/AVAILABLE)
    @PostMapping("/update")
    public ResponseEntity<Seat> updateSeatStatus(@RequestBody UpdateSeatRequestDTO req) {
        LocalDate date = req.getDepartureDate();
        Seat seat = seatService.updateSeatStatus(
                req.getTrainId(), date, req.getSeatNumber(), req.getStatus()
        );
        return ResponseEntity.ok(seat);
    }
}
