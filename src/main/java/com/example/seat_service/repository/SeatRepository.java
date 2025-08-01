package com.example.seat_service.repository;

import com.example.seat_service.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {
    List<Seat> findByTrainIdAndDepartureDate(UUID trainId, LocalDate departureDate);
    boolean existsByTrainIdAndDepartureDateAndSeatNumber(UUID trainId, LocalDate date, String seatNumber);
    Optional<Seat> findByTrainIdAndDepartureDateAndSeatNumber(UUID trainId, LocalDate date, String seatNumber);
    // REMOVE this:
    // public Seat updateSeatStatus(UUID trainId, LocalDate departureDate, String seatNumber, String status);
}
