package com.example.fileme.Repository;

import com.example.fileme.Entity.FileOtpMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileOtpRepository extends JpaRepository<FileOtpMap,Long> {

    @Query("select i.fileName from FileOtpMap i where i.otp =:otp")
    String getFileName(@Param("otp") String otp);
}
