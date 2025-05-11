package com.springaws.s3_file_uploader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {
    
    private final S3Client s3Client;
    
    @Value("${aws.s3.bucket}")
    private String bucketName;
    
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }
    
    public String uploadFile(MultipartFile file) throws IOException {
        // Generate a unique file name to avoid overwriting existing files
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        
        // Create the request for S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();
        
        // Upload the file to S3
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        
        // Return the URL of the uploaded file
        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }
}