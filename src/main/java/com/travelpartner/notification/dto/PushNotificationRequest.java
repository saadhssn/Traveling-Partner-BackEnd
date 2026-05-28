package com.travelpartner.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PushNotificationRequest {

    private String title;

    private String body;

    private String time;

    private String token;
}