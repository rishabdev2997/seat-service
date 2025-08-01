package com.example.seat_service.service;

import com.example.seat_service.model.Seat;
import com.example.seat_service.model.SeatStatus;
import com.example.seat_service.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    public List<Seat> getSeatsForTrainOnDate(UUID trainId, LocalDate departureDate) {
        return seatRepository.findByTrainIdAndDepartureDate(trainId, departureDate);
    }

    public Seat createSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    // Batch-initialize seats 1..totalSeats for train/date
    public List<Seat> initializeSeats(UUID trainId, LocalDate departureDate, int totalSeats) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            String seatNumber = String.valueOf(i);
            if (!seatRepository.existsByTrainIdAndDepartureDateAndSeatNumber(trainId, departureDate, seatNumber)) {
                Seat seat = Seat.builder()
                        .trainId(trainId)
                        .departureDate(departureDate)
                        .seatNumber(seatNumber)
                        .status(SeatStatus.AVAILABLE)
                        .build();
                seats.add(seat);
            }
        }
        return seatRepository.saveAll(seats);
    }

    public boolean seatsExist(UUID trainId, LocalDate departureDate) {
        return !seatRepository.findByTrainIdAndDepartureDate(trainId, departureDate).isEmpty();
    }

    public Seat updateSeatStatus(UUID trainId, LocalDate departureDate, String seatNumber, String status) {
        Seat seat = seatRepository.findByTrainIdAndDepartureDateAndSeatNumber(trainId, departureDate, seatNumber)
                .orElseThrow(() -> new RuntimeException("Seat not found!"));
        seat.setStatus(SeatStatus.valueOf(status));
        return seatRepository.save(seat);
    }




}
