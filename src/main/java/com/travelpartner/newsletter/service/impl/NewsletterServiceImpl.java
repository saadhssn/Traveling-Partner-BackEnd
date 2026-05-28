package com.travelpartner.newsletter.service.impl;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.newsletter.dto.NewsletterDto;
import com.travelpartner.newsletter.enums.NewsletterStatus;
import com.travelpartner.newsletter.model.Newsletter;
import com.travelpartner.newsletter.repository.NewsletterRepository;
import com.travelpartner.newsletter.service.NewsletterService;
import com.travelpartner.role.model.Role;
import com.travelpartner.role.repository.RoleRepository;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsletterServiceImpl implements NewsletterService {

    private final NewsletterRepository newsletterRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /* ================= CREATE ================= */

    @Override
    public ApiResponse<NewsletterDto> create(NewsletterDto dto) {

        User user = null;
        Role role = null;

        if (dto.getUserId() != null) {

            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // AUTO PICK ROLE FROM USER
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                role = user.getRoles().iterator().next();
            }
        }

        Newsletter newsletter = Newsletter.builder()
                .message(dto.getMessage())
                .attachedFile(dto.getAttachedFile())
                .user(user)

                // AUTO PICK USER NAME
                .userName(user != null ? user.getName() : null)

                // AUTO PICK ROLE
                .role(role)

                .status(
                        dto.getStatus() != null
                                ? NewsletterStatus.valueOf(dto.getStatus().toUpperCase())
                                : NewsletterStatus.DRAFT
                )
                .isDeleted(false)
                .build();

        return ApiResponse.success(
                "Newsletter created successfully",
                mapToDto(newsletterRepository.save(newsletter))
        );
    }

    /* ================= GET ALL ================= */

    @Override
    public ApiResponse<Page<NewsletterDto>> getAll(
            int page,
            int size,
            String search,
            String status
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        NewsletterStatus newsletterStatus = null;

        if (status != null && !status.trim().isEmpty()) {
            newsletterStatus = NewsletterStatus.valueOf(status.toUpperCase());
        }

        Page<Newsletter> newsletters =
                newsletterRepository.filterNewsletters(
                        search,
                        newsletterStatus,
                        pageable
                );

        return ApiResponse.success(
                "Newsletters fetched successfully",
                newsletters.map(this::mapToDto)
        );
    }

    /* ================= WEBSITE ================= */

    @Override
    public ApiResponse<Page<NewsletterDto>> getAllForWebsite(
            int page,
            int size
    ) {

        return getAll(page, size, null, "PUBLISHED");
    }

    /* ================= GET BY ID ================= */

    @Override
    public ApiResponse<NewsletterDto> getById(Long id) {

        Newsletter newsletter =
                newsletterRepository.findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() ->
                                new RuntimeException("Newsletter not found"));

        return ApiResponse.success(
                "Newsletter fetched successfully",
                mapToDto(newsletter)
        );
    }

    /* ================= GET BY ID WEBSITE ================= */

    @Override
    public ApiResponse<NewsletterDto> getByIdForWebsite(Long id) {

        Newsletter newsletter =
                newsletterRepository.findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() ->
                                new RuntimeException("Newsletter not found"));

        if (newsletter.getStatus() != NewsletterStatus.PUBLISHED) {
            throw new RuntimeException("Newsletter is not published");
        }

        return ApiResponse.success(
                "Newsletter fetched successfully",
                mapToDto(newsletter)
        );
    }

    /* ================= UPDATE ================= */

    @Override
    public ApiResponse<NewsletterDto> update(Long id, NewsletterDto dto) {

        Newsletter newsletter =
                newsletterRepository.findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() ->
                                new RuntimeException("Newsletter not found"));

        /* ================= UPDATE ONLY IF NOT NULL ================= */

        if (dto.getMessage() != null &&
                !dto.getMessage().trim().isEmpty()) {

            newsletter.setMessage(dto.getMessage());
        }

        if (dto.getAttachedFile() != null &&
                !dto.getAttachedFile().trim().isEmpty()) {

            newsletter.setAttachedFile(dto.getAttachedFile());
        }

        /* ================= USER + AUTO ROLE + AUTO USERNAME ================= */

        if (dto.getUserId() != null) {

            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() ->
                            new RuntimeException("User not found"));

            newsletter.setUser(user);

            // AUTO UPDATE USER NAME
            newsletter.setUserName(user.getName());

            // AUTO UPDATE ROLE
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {

                Role role = user.getRoles().iterator().next();

                newsletter.setRole(role);
            }
        }

        /* ================= STATUS ================= */

        if (dto.getStatus() != null &&
                !dto.getStatus().trim().isEmpty()) {

            newsletter.setStatus(
                    NewsletterStatus.valueOf(
                            dto.getStatus().trim().toUpperCase()
                    )
            );
        }

        return ApiResponse.success(
                "Newsletter updated successfully",
                mapToDto(newsletterRepository.save(newsletter))
        );
    }
    
    /* ================= DELETE ================= */

    @Override
    public ApiResponse<Void> delete(Long id) {

        Newsletter newsletter =
                newsletterRepository.findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() ->
                                new RuntimeException("Newsletter not found"));

        newsletter.setIsDeleted(true);
        newsletter.setDeletedAt(java.time.LocalDateTime.now());

        newsletterRepository.save(newsletter);

        return ApiResponse.success(
                "Newsletter deleted successfully",
                null
        );
    }

    /* ================= MAPPER ================= */

    private NewsletterDto mapToDto(Newsletter entity) {

        return NewsletterDto.builder()
                .id(entity.getId())
                .message(entity.getMessage())
                .attachedFile(entity.getAttachedFile())
                .userId(
                        entity.getUser() != null
                                ? entity.getUser().getId()
                                : null
                )
                .userName(entity.getUserName())

                .userRole(
                        entity.getRole() != null
                                ? entity.getRole().getName()
                                : null
                )
                .status(
                        entity.getStatus() != null
                                ? entity.getStatus().name()
                                : null
                )
                .build();
    }

}