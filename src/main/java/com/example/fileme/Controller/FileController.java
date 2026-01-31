package com.example.fileme.Controller;

import com.example.fileme.Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;


@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class FileController {

    private final Path storagePath = Paths.get("files_storage");

    @Autowired
    S3Service s3Service;

    public FileController() throws IOException{
        if(Files.notExists(storagePath)){
            Files.createDirectory(storagePath);
        }
    }

    //s3 storage
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException{
        if(file.isEmpty()){
            return ResponseEntity.badRequest().body("File is empty");
        }
        else{
            s3Service.uploadFileToS3(file);
            return ResponseEntity.ok().body("File uploaded successfully");
        }
    }

}
