package com.example.fileme.Repository;

import com.example.fileme.Entity.PendingUsers;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingUserRepo extends JpaRepository<PendingUsers,Long> {

    @Query("select i from PendingUsers i where i.username =:username")
    PendingUsers findByUsername(@Param("username") String username);

    @Query("select i from PendingUsers i where i.email =:email")
    PendingUsers findByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("delete from PendingUsers i where i.email =:email")
    void deleteByEmail(@Param("email") String email);
}
