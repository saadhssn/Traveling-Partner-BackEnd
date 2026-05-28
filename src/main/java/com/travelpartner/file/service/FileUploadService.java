package com.travelpartner.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final S3Client s3Client;

    @Value("${digitalocean.spaces.bucket}")
    private String bucketName;

    @Value("${digitalocean.spaces.base-url}")
    private String baseUrl;

    public String uploadFile(MultipartFile file, String folder) {

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String key = folder + "/" + fileName;

            // Compress image if it's an image
            byte[] data;
            if (file.getContentType() != null && file.getContentType().startsWith("image/")) {
                try (InputStream is = file.getInputStream();
                     ByteArrayOutputStream os = new ByteArrayOutputStream()) {

                    Thumbnails.of(is)
                            .size(1024, 1024)        // Resize max width/height
                            .outputQuality(0.7)       // Reduce quality (70%)
                            .toOutputStream(os);

                    data = os.toByteArray();
                }
            } else {
                // Non-image files remain unchanged
                data = file.getBytes();
            }

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(data)
            );

            return baseUrl + "/" + key;

        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }
}