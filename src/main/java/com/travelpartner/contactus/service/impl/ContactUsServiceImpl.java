package com.travelpartner.contactus.service.impl;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.contactus.dto.ContactUsDto;
import com.travelpartner.contactus.model.ContactUs;
import com.travelpartner.contactus.repository.ContactUsRepository;
import com.travelpartner.contactus.service.ContactUsService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactUsServiceImpl implements ContactUsService {

    private final ContactUsRepository repository;
    private final JavaMailSender mailSender;

    @Override
    public ApiResponse<Void> submitFromApp(ContactUsDto dto) {

        saveContact(dto, "APP");

        sendEmail(dto, "APP");

        return ApiResponse.success(
                "App contact request submitted successfully",
                null
        );
    }

    @Override
    public ApiResponse<Void> submitFromWebsite(ContactUsDto dto) {

        saveContact(dto, "WEBSITE");

        sendEmail(dto, "WEBSITE");

        return ApiResponse.success(
                "Website contact request submitted successfully",
                null
        );
    }

    private void saveContact(
            ContactUsDto dto,
            String source
    ) {

        ContactUs contact = ContactUs.builder()

                .name(getSafeValue(dto.getName(), "Anonymous"))
                .email(getSafeValue(dto.getEmail(), "N/A"))
                .subject(getSafeValue(dto.getSubject(), "No Subject"))
                .message(getSafeValue(dto.getMessage(), "No Message"))
                .phoneNumber(getSafeValue(dto.getPhoneNumber(), "N/A"))
                .photoPath(getSafeValue(dto.getPhoto(), null))

                .build();

        repository.save(contact);
    }

    private String getSafeValue(
            String value,
            String defaultValue
    ) {

        return (value == null || value.trim().isEmpty())
                ? defaultValue
                : value.trim();
    }

    @Async
    public void sendEmail(
            ContactUsDto dto,
            String source
    ) {

        try {

            SimpleMailMessage mailMessage =
                    new SimpleMailMessage();

            mailMessage.setTo(
                    "travelingpartnernetwork@gmail.com"
            );

            String subject =
                    getSafeValue(
                            dto.getSubject(),
                            "No Subject"
                    );

            mailMessage.setSubject(
                    "[" + source + "] New Contact Request: "
                            + subject
            );

            String emailBody = String.format(
                    """
                    Source: %s
                    
                    Name: %s
                    
                    Email: %s
                    
                    Phone: %s
                    
                    Subject: %s
                    
                    Message:
                    %s
                    
                    Photo:
                    %s
                    """,

                    source,

                    getSafeValue(dto.getName(), "Anonymous"),

                    getSafeValue(dto.getEmail(), "N/A"),

                    getSafeValue(dto.getPhoneNumber(), "N/A"),

                    subject,

                    getSafeValue(dto.getMessage(), "No Message"),

                    getSafeValue(dto.getPhoto(), "N/A")
            );

            mailMessage.setText(emailBody);

            mailSender.send(mailMessage);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}