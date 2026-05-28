package com.travelpartner.chat.dto;

import com.travelpartner.chat.enums.MessageType;
import lombok.*;

@Getter
@Setter
public class SendMessageRequest {
    private Long receiverId;
    private String content;
    private MessageType messageType;
}