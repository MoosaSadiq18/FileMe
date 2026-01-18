package com.example.fileme.Repository;

import com.example.fileme.Entity.UserSignUpInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserSignUpInfo,Long> {

    @Query("select i from UserSignUpInfo i where i.username =:username")
    UserSignUpInfo findByUsername(@Param("username") String username);

    @Query("select i from UserSignUpInfo i where i.email =:email")
    UserSignUpInfo findByEmail(@Param("email") String email);

    @Modifying
    @Query("delete from UserSignUpInfo i where i.email =:email")
    void deleteByEmail(@Param("email") String email);

}
