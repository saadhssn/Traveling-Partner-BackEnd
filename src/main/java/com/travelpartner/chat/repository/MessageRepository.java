package com.travelpartner.chat.repository;

import com.travelpartner.chat.model.Message;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
SELECT m FROM Message m
WHERE m.chatRoomId = :chatRoomId
AND m.isDeleted = false
ORDER BY m.createdAt ASC
""")
    Page<Message> findMessages(@Param("chatRoomId") Long chatRoomId, Pageable pageable);
}