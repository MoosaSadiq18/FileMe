package com.example.fileme.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LoggerDto {
    private String uploadedAt;
    private String filename;
    private long fileSize;

    public void setLoggerData(String uploadedAt,String filename,long fileSize){
        this.uploadedAt = uploadedAt;
        this.filename = filename;
        this.fileSize = fileSize;
    }
}
