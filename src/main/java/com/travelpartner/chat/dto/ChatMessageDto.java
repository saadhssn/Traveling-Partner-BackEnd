package com.travelpartner.chat.dto;

import com.travelpartner.chat.enums.MessageStatus;
import com.travelpartner.chat.enums.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChatMessageDto {

    private Long id;

    private Long senderId;
    private String senderName;

    private Long receiverId;
    private String receiverName;

    private String content;

    private String mediaUrl;

    private MessageType messageType;

    private MessageStatus status;

    private LocalDateTime createdAt;
}