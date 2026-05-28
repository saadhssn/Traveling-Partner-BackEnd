package com.travelpartner.chat.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelpartner.chat.dto.*;
import com.travelpartner.chat.enums.MessageStatus;
import com.travelpartner.chat.model.ChatRoom;
import com.travelpartner.chat.model.Message;
import com.travelpartner.chat.repository.ChatRoomRepository;
import com.travelpartner.chat.repository.MessageRepository;
import com.travelpartner.chat.service.ChatService;
import com.travelpartner.chat.websocket.WebSocketSessionManager;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.role.model.Role;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepo;
    private final MessageRepository messageRepo;
    private final UserRepository userRepository;
    private final WebSocketSessionManager sessionManager;
    private final ObjectMapper objectMapper;

    @Override
    public List<Message> getAllMessages() {
        return messageRepo.findAll();
    }

    @Override
    public ApiResponse<ChatMessageDto> sendMessage(Long senderId, SendMessageRequest request) {

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        boolean senderIsPartner = hasRole(sender, "PARTNER");
        boolean senderIsDriver = hasRole(sender, "DRIVER");

        Long partnerId;
        Long driverId;

        if (senderIsPartner) {

            partnerId = senderId;
            driverId = receiver.getId();

        } else if (senderIsDriver) {

            driverId = senderId;
            partnerId = receiver.getId();

        } else {
            throw new RuntimeException("Invalid chat role");
        }

        ChatRoom room = chatRoomRepo.findRoom(partnerId, driverId)
                .orElseGet(() ->
                        chatRoomRepo.save(
                                ChatRoom.builder()
                                        .partnerId(partnerId)
                                        .driverId(driverId)
                                        .build()
                        )
                );

        Message message = Message.builder()
                .chatRoomId(room.getId())
                .senderId(senderId)
                .receiverId(request.getReceiverId())
                .content(request.getContent())
                .messageType(request.getMessageType())
                .status(MessageStatus.SENT)
                .build();

        Message saved = messageRepo.save(message);

        room.setLastMessageId(saved.getId());
        chatRoomRepo.save(room);

        try {

            ChatMessageDto dto = map(saved);

            WebSocketSession receiverSession =
                    sessionManager.getSession(request.getReceiverId());

            if (receiverSession != null && receiverSession.isOpen()) {

                receiverSession.sendMessage(
                        new TextMessage(
                                objectMapper.writeValueAsString(dto)
                        )
                );
            }

            WebSocketSession senderSession =
                    sessionManager.getSession(senderId);

            if (senderSession != null && senderSession.isOpen()) {

                senderSession.sendMessage(
                        new TextMessage(
                                objectMapper.writeValueAsString(dto)
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ApiResponse.success("Message sent", map(saved));
    }

    @Override
    public ApiResponse<Page<ChatMessageDto>> getMessages(
            Long partnerId,
            Long driverId,
            int page,
            int size
    ) {

        ChatRoom room = chatRoomRepo.findRoom(partnerId, driverId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        Page<Message> messages = messageRepo.findMessages(
                room.getId(),
                PageRequest.of(page, size, Sort.by("createdAt").ascending())
        );

        return ApiResponse.success(
                "Messages fetched",
                messages.map(this::map)
        );
    }

    @Override
    public ApiResponse<Void> markAsRead(Long messageId) {

        Message msg = messageRepo.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        msg.setStatus(MessageStatus.READ);

        messageRepo.save(msg);

        return ApiResponse.success(
                "Message marked as read",
                null
        );
    }

    @Override
    public ApiResponse<Page<ChatMessageDto>> getMessagesByRoom(
            Long chatRoomId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").ascending()
        );

        Page<Message> messages =
                messageRepo.findMessages(chatRoomId, pageable);

        return ApiResponse.success(
                "Messages fetched successfully",
                messages.map(this::map)
        );
    }

    private boolean hasRole(User user, String roleName) {

        Set<Role> roles = user.getRoles();

        return roles.stream()
                .map(Role::getName)
                .anyMatch(r ->
                        r.equalsIgnoreCase(roleName)
                );
    }

    private ChatMessageDto map(Message m) {

        User sender = userRepository.findById(m.getSenderId())
                .orElse(null);

        User receiver = userRepository.findById(m.getReceiverId())
                .orElse(null);

        String senderName = null;
        String receiverName = null;

        if (sender != null && sender.getBasicInformation() != null) {

            senderName =
                    sender.getBasicInformation().getFirstName()
                            + " "
                            + sender.getBasicInformation().getLastName();
        }

        if (receiver != null && receiver.getBasicInformation() != null) {

            receiverName =
                    receiver.getBasicInformation().getFirstName()
                            + " "
                            + receiver.getBasicInformation().getLastName();
        }

        return ChatMessageDto.builder()
                .id(m.getId())
                .senderId(m.getSenderId())
                .senderName(senderName)
                .receiverId(m.getReceiverId())
                .receiverName(receiverName)
                .content(m.getContent())
                .mediaUrl(m.getMediaUrl())
                .messageType(m.getMessageType())
                .status(m.getStatus())
                .createdAt(m.getCreatedAt())
                .build();
    }

    @Override
    public ApiResponse<List<ChatRoomDto>> getUserChatRooms(Long userId) {

        List<ChatRoom> rooms = chatRoomRepo.findAllByUserId(userId);

        List<ChatRoomDto> response = rooms.stream()
                .map(room -> {

                    User partner = userRepository.findById(room.getPartnerId())
                            .orElse(null);

                    User driver = userRepository.findById(room.getDriverId())
                            .orElse(null);

                    String partnerName = null;
                    String driverName = null;

                    if (partner != null && partner.getBasicInformation() != null) {

                        partnerName =
                                partner.getBasicInformation().getFirstName()
                                        + " "
                                        + partner.getBasicInformation().getLastName();
                    }

                    if (driver != null && driver.getBasicInformation() != null) {

                        driverName =
                                driver.getBasicInformation().getFirstName()
                                        + " "
                                        + driver.getBasicInformation().getLastName();
                    }

                    String lastMessage = null;

                    if (room.getLastMessageId() != null) {

                        lastMessage = messageRepo.findById(room.getLastMessageId())
                                .map(Message::getContent)
                                .orElse(null);
                    }

                    return ChatRoomDto.builder()
                            .chatRoomId(room.getId())
                            .partnerId(room.getPartnerId())
                            .partnerName(partnerName)
                            .driverId(room.getDriverId())
                            .driverName(driverName)
                            .lastMessageId(room.getLastMessageId())
                            .lastMessage(lastMessage)
                            .build();
                })
                .toList();

        return ApiResponse.success(
                "Chat rooms fetched successfully",
                response
        );
    }
}