package com.example.fileme.Service;

import com.example.fileme.Entity.FileOtpMap;
import com.example.fileme.Repository.FileOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FileOtpService {
    public String getFileOtp(){
        int min = 100000;
        int max = 999999;
        return String.valueOf(new Random().nextInt(max - min + 1) + min);
    }

    @Autowired
    FileOtpRepository fileOtpRepository;

    @Autowired
    FileOtpMap fileOtpMap;

    public void mapFileOtp(String fileName){
        fileOtpMap.setFileName(fileName);
        fileOtpMap.setOtp(getFileOtp());
        fileOtpRepository.save(fileOtpMap);
    }

    public String getFileNameFromOtp(String otp){
        return fileOtpRepository.getFileName(otp);
    }

}
