package com.example.fileme.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final Map<String,String> pinStore = new ConcurrentHashMap<>();

    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public String generatedPin(){
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        return String.valueOf(new Random().nextInt(max - min + 1) + min);
    }

    public boolean confirmPin(String email,String inputPin){
        String storedPin = pinStore.get(email);
        if(storedPin == null){
            return false;
        }
        return storedPin.equals(inputPin);
    }

    public void sendEmail(String emailId){
        String pin = generatedPin();
        pinStore.put(emailId,pin);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailId);
        message.setSubject("FileMe account verification");
        message.setText("Your account verification pin is " + pin);
        javaMailSender.send(message);
    }

}