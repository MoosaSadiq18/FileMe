package com.example.fileme.Controller;

import com.example.fileme.Entity.FileMetaData;
import com.example.fileme.Entity.FileOtpMap;
import com.example.fileme.Service.EmailService;
import com.example.fileme.Service.FileOtpService;
import com.example.fileme.Service.LoggerService;
import com.example.fileme.Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;


@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class FileController {

    private final Path storagePath = Paths.get("files_storage");

    @Autowired
    S3Service s3Service;
    @Autowired
    private EmailService emailService;

    public FileController() throws IOException{
        if(Files.notExists(storagePath)){
            Files.createDirectory(storagePath);
        }
    }

    @Autowired
    LoggerService loggerService;

    @Autowired
    FileMetaData fileMetaData;

    @Autowired
    FileOtpService fileOtpService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,@RequestParam String emailId) throws IOException{
        if(file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        else{
            LocalDateTime currentTime = LocalDateTime.now();
            fileMetaData.setLoggerData(currentTime.toString(),file.getOriginalFilename(),file.getSize());
            loggerService.saveToRepository(fileMetaData);
            fileOtpService.mapFileOtp(file.getOriginalFilename());
            s3Service.uploadFileToS3(file);
            emailService.sendFileOtpEmail(emailId,fileOtpService.getOtpFromFileName(file.getOriginalFilename()));
            return ResponseEntity.ok().body("File uploaded successfully");
        }
    }

    
    @GetMapping("/download/{otp}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String otp){
        byte[] data = s3Service.downloadFileFromS3(otp);
        String filename = fileOtpService.getFileNameFromOtp(otp);
        String contentType = URLConnection.guessContentTypeFromName(filename);

        if(contentType==null){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(filename).build().toString())
                .body(data);
    }

}