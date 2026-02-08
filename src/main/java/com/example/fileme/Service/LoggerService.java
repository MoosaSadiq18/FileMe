package com.example.fileme.Service;

import com.example.fileme.Dto.LoggerDto;
import com.example.fileme.Entity.FileMetaData;
import com.example.fileme.Repository.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {

    @Autowired
    LoggerRepository loggerRepository;

    public void saveToRepository(FileMetaData fileMetaData){
        loggerRepository.save(fileMetaData);
    }

}
