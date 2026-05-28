package com.travelpartner.notification.service;

import com.google.firebase.messaging.*;
import com.travelpartner.notification.dto.PushNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebasePushNotificationService {

    public void sendPushNotification(
            PushNotificationRequest request
    ) {

        try {

            if (request.getToken() == null ||
                    request.getToken().isBlank()) {

                System.out.println("FCM token is empty");
                return;
            }

            Notification notification =
                    Notification.builder()
                            .setTitle(request.getTitle())
                            .setBody(request.getBody())
                            .build();

            Message message = Message.builder()

                    .setToken(request.getToken().trim())

                    .setNotification(notification)

                    .putData("time", request.getTime())

                    .putData(
                            "click_action",
                            "FLUTTER_NOTIFICATION_CLICK"
                    )

                    .build();

            String response =
                    FirebaseMessaging.getInstance()
                            .send(message);

            System.out.println(
                    "Push notification sent successfully: "
                            + response
            );

        } catch (FirebaseMessagingException e) {

            System.out.println(
                    "Firebase error: " + e.getMessage()
            );

            // INVALID TOKEN
            if (e.getMessagingErrorCode() ==
                    MessagingErrorCode.INVALID_ARGUMENT ||

                    e.getMessagingErrorCode() ==
                            MessagingErrorCode.UNREGISTERED) {

                System.out.println(
                        "Invalid or expired FCM token"
                );

                // OPTIONAL:
                // remove token from DB
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}