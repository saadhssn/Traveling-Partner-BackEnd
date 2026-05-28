package com.travelpartner.notification.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocketEvent<T> {

    private String type; // NEW_RIDE
    private T data;
}