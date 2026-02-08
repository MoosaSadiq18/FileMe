package com.example.fileme.Dto;

import com.example.fileme.Entity.UserSignUpInfo;
import com.example.fileme.Repository.PendingUserRepo;
import com.example.fileme.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Migrator {

    @Autowired
    private PendingUserRepo pendingUserRepo;

    @Autowired
    private UserRepository userRepository;

    public void migrateUser(String email){
        UserSignUpInfo user = new UserSignUpInfo();
        user.setEmail(email);
        user.setUsername((pendingUserRepo.findByEmail(email)).getUsername());
        user.setPassword((pendingUserRepo.findByEmail(email)).getPassword());
        userRepository.save(user);
    }
}
