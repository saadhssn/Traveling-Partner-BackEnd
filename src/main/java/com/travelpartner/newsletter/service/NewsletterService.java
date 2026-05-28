package com.travelpartner.newsletter.service;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.newsletter.dto.NewsletterDto;
import org.springframework.data.domain.Page;

public interface NewsletterService {

    ApiResponse<NewsletterDto> create(NewsletterDto dto);

    ApiResponse<Page<NewsletterDto>> getAll(
            int page,
            int size,
            String search,
            String status
    );

    ApiResponse<Page<NewsletterDto>> getAllForWebsite(
            int page,
            int size
    );

    ApiResponse<NewsletterDto> getById(Long id);

    ApiResponse<NewsletterDto> getByIdForWebsite(Long id);

    ApiResponse<NewsletterDto> update(Long id, NewsletterDto dto);

    ApiResponse<Void> delete(Long id);

}