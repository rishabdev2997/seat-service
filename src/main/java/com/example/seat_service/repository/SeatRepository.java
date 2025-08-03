package com.example.seat_service.repository;

import com.example.seat_service.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {

    List<Seat> findByTrainIdAndDepartureDate(UUID trainId, LocalDate departureDate);

    boolean existsByTrainIdAndDepartureDateAndSeatNumber(UUID trainId, LocalDate departureDate, String seatNumber);

    Optional<Seat> findByTrainIdAndDepartureDateAndSeatNumber(UUID trainId, LocalDate departureDate, String seatNumber);

    /**
     * Deletes all Seat records with departure date less than the given cutoff date.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Seat s WHERE s.departureDate < :cutoffDate")
    void deleteByDepartureDateBefore(@Param("cutoffDate") LocalDate cutoffDate);

    // Removed the commented-out update method as requested.
}
