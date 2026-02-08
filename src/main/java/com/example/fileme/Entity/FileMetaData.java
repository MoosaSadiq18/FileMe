package com.example.fileme.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class FileMetaData {

    private String uploadedAt;
    private String filename;
    private long fileSize;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public void setLoggerData(String uploadedAt,String filename,long fileSize){
        this.uploadedAt = uploadedAt;
        this.filename = filename;
        this.fileSize = fileSize;
    }

}
