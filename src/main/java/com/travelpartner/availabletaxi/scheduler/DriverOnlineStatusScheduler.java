package com.travelpartner.availabletaxi.scheduler;

import com.travelpartner.availabletaxi.enums.DriverStatus;
import com.travelpartner.availabletaxi.model.AvailableTaxi;
import com.travelpartner.availabletaxi.repository.AvailableTaxiRepository;
import com.travelpartner.notification.dto.PushNotificationRequest;
import com.travelpartner.notification.service.FirebasePushNotificationService;
import com.travelpartner.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DriverOnlineStatusScheduler {

    private final AvailableTaxiRepository availableTaxiRepository;

    private final FirebasePushNotificationService firebasePushNotificationService;

    /**
     * Every 5 minutes check who completed 3 hours online
     */
    @Scheduled(fixedRate = 300000)
    public void checkDriverOnlineDuration() {

        LocalDateTime threshold =
                LocalDateTime.now().minusHours(3);

        List<AvailableTaxi> drivers =
                availableTaxiRepository
                        .findDriversNeedingConfirmation(threshold);

        for (AvailableTaxi taxi : drivers) {

            User driver = taxi.getDriver();

            if (driver.getDeviceToken() != null &&
                    !driver.getDeviceToken().isBlank()) {

                firebasePushNotificationService.sendPushNotification(

                        PushNotificationRequest.builder()
                                .title("Traveling Partner")
                                .body("Are you still online?")
                                .token(driver.getDeviceToken())
                                .time(LocalDateTime.now().toString())
                                .build()
                );
            }

            taxi.setAwaitingConfirmation(true);

            taxi.setConfirmationSentAt(LocalDateTime.now());

            availableTaxiRepository.save(taxi);

            log.info(
                    "Confirmation notification sent to driver {}",
                    driver.getId()
            );
        }
    }

    /**
     * Auto offline after 5 minutes no response
     */
    @Scheduled(fixedRate = 60000)
    public void autoOfflineDrivers() {

        LocalDateTime threshold =
                LocalDateTime.now().minusMinutes(5);

        List<AvailableTaxi> drivers =
                availableTaxiRepository
                        .findDriversForAutoOffline(threshold);

        for (AvailableTaxi taxi : drivers) {

            taxi.setDriverStatus(DriverStatus.OFFLINE);

            taxi.setAwaitingConfirmation(false);

            taxi.setOnlineSince(null);

            taxi.setConfirmationSentAt(null);

            availableTaxiRepository.save(taxi);

            log.info(
                    "Driver auto OFFLINE {}",
                    taxi.getDriver().getId()
            );
        }
    }
}