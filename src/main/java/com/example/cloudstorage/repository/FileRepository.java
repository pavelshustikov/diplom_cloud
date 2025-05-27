package com.example.cloudstorage.repository;

import com.example.cloudstorage.entity.FileEntity;
import com.example.cloudstorage.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByUser(AppUser user);
    FileEntity findByFilenameAndUser(String filename, AppUser user);
}