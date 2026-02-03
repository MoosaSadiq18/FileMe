package com.example.fileme.Controller;

import com.example.fileme.Service.S3Service;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename){
        byte[] data = s3Service.downloadFileFromS3(filename);
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