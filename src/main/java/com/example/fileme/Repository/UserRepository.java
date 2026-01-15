package com.example.fileme.Repository;

import com.example.fileme.Entity.UserSignUpInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserSignUpInfo,Long> {
    UserSignUpInfo findByUsername(String username);

    UserSignUpInfo findByEmail(String email);
}
