package com.travelpartner.chat.service;

import com.travelpartner.chat.dto.*;
import com.travelpartner.chat.model.Message;
import com.travelpartner.common.response.ApiResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChatService {

    ApiResponse<ChatMessageDto> sendMessage(Long senderId, SendMessageRequest request);

    ApiResponse<Page<ChatMessageDto>> getMessages(
            Long partnerId,
            Long driverId,
            int page,
            int size
    );

    ApiResponse<Void> markAsRead(Long messageId);

    List<Message> getAllMessages();

    ApiResponse<Page<ChatMessageDto>> getMessagesByRoom(
            Long chatRoomId,
            int page,
            int size
    );

    ApiResponse<List<ChatRoomDto>> getUserChatRooms(Long userId);
}