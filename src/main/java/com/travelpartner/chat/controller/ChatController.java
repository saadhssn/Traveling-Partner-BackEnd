package com.travelpartner.chat.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.chat.dto.*;
import com.travelpartner.chat.model.Message;
import com.travelpartner.chat.service.ChatService;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.config.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final JwtService jwtService;

    @PostMapping("/send")
    @AuditAction(action = "CREATE", module = "CHAT", description = "Send chat message")
    public ApiResponse<ChatMessageDto> send(
            @RequestHeader("Authorization") String token,
            @RequestBody SendMessageRequest request
    ) {
        Long senderId = extractUserId(token);
        return chatService.sendMessage(senderId, request);
    }

    @GetMapping("/messages")
    @AuditAction(action = "READ", module = "CHAT", description = "Get chat messages between partner and driver")
    public ApiResponse<Page<ChatMessageDto>> getMessages(
            @RequestParam Long partnerId,
            @RequestParam Long driverId,
            @RequestParam(defaultValue = "0") int page
    ) {
        return chatService.getMessages(partnerId, driverId, page, 20);
    }

    @PutMapping("/read/{id}")
    @AuditAction(action = "UPDATE", module = "CHAT", description = "Mark message as read")
    public ApiResponse<Void> read(@PathVariable Long id) {
        return chatService.markAsRead(id);
    }

    private Long extractUserId(String token) {
        token = token.replace("Bearer ", "");
        return jwtService.extractUserId(token);
    }

    @GetMapping("/getAll")
    @AuditAction(action = "READ", module = "CHAT", description = "Get all chat messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(chatService.getAllMessages());
    }

    @GetMapping("/room/{chatRoomId}/messages")
    @AuditAction(action = "READ", module = "CHAT", description = "Get messages by chat room")
    public ApiResponse<Page<ChatMessageDto>> getMessagesByRoom(
            @PathVariable Long chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return chatService.getMessagesByRoom(chatRoomId, page, size);
    }

    @GetMapping("/rooms")
    @AuditAction(action = "READ", module = "CHAT", description = "Get all chat rooms of user")
    public ApiResponse<List<ChatRoomDto>> getUserChatRooms(
            @RequestHeader("Authorization") String token
    ) {

        Long userId = extractUserId(token);

        return chatService.getUserChatRooms(userId);
    }
}