package com.example.fileme.Repository;

import com.example.fileme.Dto.LoggerDto;
import com.example.fileme.Entity.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggerRepository extends JpaRepository<FileMetaData,Long> {
}
