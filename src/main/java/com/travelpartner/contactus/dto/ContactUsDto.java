package com.travelpartner.contactus.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactUsDto {

    private String name;
    private String email;
    private String subject;
    private String message;
    private String phoneNumber;

    // Optional photo URL
    private String photo;
}