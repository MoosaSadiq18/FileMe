package com.example.fileme.Dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PendingUsers {
        private String username;
        private String password;
        private String email;
        private String onlineStatus;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
}
