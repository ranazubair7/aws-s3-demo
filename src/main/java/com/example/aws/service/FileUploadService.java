package com.example.aws.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadService {

    private S3Client s3Client;
    private final String bucketName = "zubair-bucket";

    @Autowired
    public FileUploadService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Method to upload file to S3 bucket.
     *
     * @param file the file.
     * @return response
     */
    public ResponseEntity<Object> uploadFile(MultipartFile file) {
        try {
            // to identify the object uniquely.
            String key = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            // Upload the file to S3 bucket
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build(), RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String message = "File uploaded successfully!";
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (IOException exception) {
            String message = "Exception occured while uploading the file";
            log.error(message);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }
}
