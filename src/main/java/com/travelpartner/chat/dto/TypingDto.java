package com.travelpartner.chat.dto;

import lombok.*;

@Getter
@Setter
public class TypingDto {
    private Long senderId;
    private Long receiverId;
    private boolean typing;
}