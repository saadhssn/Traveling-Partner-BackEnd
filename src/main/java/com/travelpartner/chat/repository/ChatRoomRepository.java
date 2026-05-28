package com.travelpartner.chat.repository;

import com.travelpartner.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("""
        SELECT c FROM ChatRoom c
        WHERE c.partnerId = :partnerId
        AND c.driverId = :driverId
    """)
    Optional<ChatRoom> findRoom(Long partnerId, Long driverId);

    @Query("""
    SELECT c FROM ChatRoom c
    WHERE c.partnerId = :userId
    OR c.driverId = :userId
""")
    List<ChatRoom> findAllByUserId(Long userId);
}