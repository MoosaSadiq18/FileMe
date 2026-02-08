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
public class FileOtpMap {
    private String fileName;
    private String otp;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
}
