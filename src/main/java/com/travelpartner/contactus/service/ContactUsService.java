package com.travelpartner.contactus.service;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.contactus.dto.ContactUsDto;

public interface ContactUsService {

    ApiResponse<Void> submitFromApp(ContactUsDto dto);

    ApiResponse<Void> submitFromWebsite(ContactUsDto dto);
}