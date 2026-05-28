package com.travelpartner.file.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.file.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/cnic")
    @AuditAction(action = "UPLOAD", module = "DOCUMENT", description = "Upload CNIC")
    public ApiResponse<String> uploadCnic(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadFile(file, "cnic");
        return ApiResponse.success("File uploaded successfully", url);
    }

    @PostMapping("/driving-license")
    @AuditAction(action = "UPLOAD", module = "DOCUMENT", description = "Upload driving license")
    public ApiResponse<String> uploadDrivingLicense(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadFile(file, "driving-license");
        return ApiResponse.success("File uploaded successfully", url);
    }

    @PostMapping("/vehicle-documents")
    @AuditAction(action = "UPLOAD", module = "DOCUMENT", description = "Upload vehicle documents")
    public ApiResponse<String> uploadVehicleDocuments(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadFile(file, "vehicle-documents");
        return ApiResponse.success("File uploaded successfully", url);
    }

    @PostMapping("/profile-picture")
    @AuditAction(action = "UPLOAD", module = "DOCUMENT", description = "Upload profile picture")
    public ApiResponse<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadFile(file, "profile-picture");
        return ApiResponse.success("File uploaded successfully", url);
    }

    @PostMapping("/vehicle-image")
    @AuditAction(action = "UPLOAD", module = "DOCUMENT", description = "Upload vehicle image")
    public ApiResponse<String> uploadVehicleImage(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadFile(file, "vehicle-image");
        return ApiResponse.success("File uploaded successfully", url);
    }

    @PostMapping("/Carousel")
    @AuditAction(action = "UPLOAD", module = "BANNER", description = "Upload Carousel image")
    public ApiResponse<String> uploadCarousel(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadFile(file, "banners");
        return ApiResponse.success("Carousel uploaded successfully", url);
    }

    @PostMapping("/contact-us")
    @AuditAction(
            action = "UPLOAD",
            module = "CONTACT_US",
            description = "Upload contact us document"
    )
    public ApiResponse<String> uploadContactUsDocument(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadFile(file, "contact-us");
        return ApiResponse.success("Contact document uploaded successfully", url);
    }

    @PostMapping("/news-letter")
    @AuditAction(
            action = "UPLOAD",
            module = "NEWS_LETTER",
            description = "Upload news letter document"
    )
    public ApiResponse<String> uploadNewsLetterDocument(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadFile(file, "news-letter");
        return ApiResponse.success("NEWS LETTER document uploaded successfully", url);
    }

}