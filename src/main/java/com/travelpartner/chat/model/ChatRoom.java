package com.travelpartner.chat.model;

import com.travelpartner.common.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom extends BaseEntity {

    private Long partnerId;

    private Long driverId;

    private Long lastMessageId;
}