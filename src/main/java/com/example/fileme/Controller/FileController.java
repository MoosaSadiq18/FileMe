package com.example.fileme.Controller;

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

    public FileController() throws IOException{
        if(Files.notExists(storagePath)){
            Files.createDirectory(storagePath);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile){
        if(multipartFile.isEmpty()){
            return ResponseEntity.badRequest().body("File is empty");
        }

        String fileName = multipartFile.getOriginalFilename();
        if(fileName == null || !(fileName.toLowerCase(Locale.ROOT).matches(".*\\.(jpg|jpeg|png|gif|pdf)$"))){
            return ResponseEntity.badRequest().body("Enter image files only");
        }

        String storedName = Paths.get(fileName).getFileName().toString();

        Path targetFile = storagePath.resolve(storedName).normalize();
        if(!targetFile.startsWith(storagePath)){
            return ResponseEntity.badRequest().body("Invalid file path");
        }

        try(InputStream stream = multipartFile.getInputStream()){
            Files.copy(stream, targetFile, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok().body("Image uploaded...");
        }
        catch (IOException e){
            return ResponseEntity.internalServerError().body("Image failed to upload");
        }
    }

}
