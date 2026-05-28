package com.travelpartner.newsletter.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsletterDto {

    private Long id;

    private String message;

    private String attachedFile;

    private Long userId;

    private String userName;

    private String userRole;

    private String status;

}