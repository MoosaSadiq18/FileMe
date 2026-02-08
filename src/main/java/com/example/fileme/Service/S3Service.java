package com.example.fileme.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;

@Service
public class S3Service {

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    S3Client s3Client;

    @Autowired
    FileOtpService fileOtpService;

    public void uploadFileToS3(MultipartFile file) throws IOException{
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getOriginalFilename())
                .build(),
                RequestBody.fromBytes(file.getBytes()));
    }

    public byte[] downloadFileFromS3(String otp){
        String filename = fileOtpService.getFileNameFromOtp(otp).trim();

        ResponseBytes<GetObjectResponse> objectAsBytes =
                        s3Client.getObjectAsBytes(GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(filename)
                        .build());

        return objectAsBytes.asByteArray();
    }

}
