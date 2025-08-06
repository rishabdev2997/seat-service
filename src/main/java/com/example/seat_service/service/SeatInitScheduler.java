package com.example.seat_service.service;

import com.example.seat_service.dto.TrainRunDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Scheduler component to automate seat initialization and cleanup for train runs.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SeatInitScheduler {

    private final RestTemplate restTemplate;
    private final SeatService seatService;

    /**
     * Train service URL endpoint to fetch train runs.
     * Externalized to allow configuration via environment variables or application.properties.
     */
    @Value("${train.service.url}")
    private String trainServiceUrl;

    /**
     * Scheduled task running every 2 minutes:
     * 1. Deletes seats for expired trains (departureDate < today),
     * 2. Initializes seats for upcoming train runs if missing.
     */
    @Scheduled(cron = "0 */10 * * * *", zone = "Asia/Kolkata")
    public void automateSeatInitialization() {
        log.info("🚦 SeatInitScheduler running at {}", java.time.LocalDateTime.now());

        try {
            LocalDate today = LocalDate.now();

            // 1. Cleanup expired seat data before seeding
            log.info("Starting cleanup of seats for expired trains before {}", today);
            seatService.deleteSeatsByDepartureDateBefore(today);
            log.info("Completed cleanup of expired seats.");

            // 2. Fetch upcoming train runs from train service
            ResponseEntity<List<TrainRunDTO>> response = restTemplate.exchange(
                    trainServiceUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TrainRunDTO>>() {}
            );
            List<TrainRunDTO> trainRuns = response.getBody();

            if (trainRuns == null || trainRuns.isEmpty()) {
                log.warn("No train runs returned from train-service at URL: {}", trainServiceUrl);
                return;
            }

            // 3. Initialize seats for trains where seat data is missing
            for (TrainRunDTO run : trainRuns) {
                UUID trainId = run.getId();
                LocalDate departureDate = run.getDepartureDate();
                int totalSeats = run.getTotalSeats();

                boolean exist = seatService.seatsExist(trainId, departureDate);
                if (!exist) {
                    seatService.initializeSeats(trainId, departureDate, totalSeats);
                    log.info("✅ Initialized {} seats for train {} on date {}", totalSeats, run.getTrainNumber(), departureDate);
                } else {
                    log.info("ℹ️ Seats for train {} on date {} already exist", run.getTrainNumber(), departureDate);
                }
            }
        } catch (RestClientException ex) {
            log.error("Error while fetching train runs from train-service: {}", ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Unexpected error occurred in SeatInitScheduler: {}", ex.getMessage(), ex);
        }
    }
}
