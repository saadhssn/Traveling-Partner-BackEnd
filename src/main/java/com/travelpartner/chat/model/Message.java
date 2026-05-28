package com.travelpartner.chat.model;

import com.travelpartner.chat.enums.MessageStatus;
import com.travelpartner.chat.enums.MessageType;
import com.travelpartner.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message extends BaseEntity {

    private Long chatRoomId;

    private Long senderId;
    private Long receiverId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Builder.Default
    private Boolean isDeleted = false;
}