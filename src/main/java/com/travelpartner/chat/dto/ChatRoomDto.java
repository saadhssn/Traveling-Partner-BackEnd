package com.travelpartner.chat.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {

    private Long chatRoomId;

    private Long partnerId;
    private String partnerName;

    private Long driverId;
    private String driverName;

    private Long lastMessageId;

    private String lastMessage;
}