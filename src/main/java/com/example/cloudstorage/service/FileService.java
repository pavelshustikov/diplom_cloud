package com.example.cloudstorage.service;

import com.example.cloudstorage.dto.FileResponse;
import com.example.cloudstorage.entity.AppUser;
import com.example.cloudstorage.entity.FileEntity;
import com.example.cloudstorage.repository.FileRepository;
import com.example.cloudstorage.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Value("${storage.location}")
    private String storageLocation;

    private final FileRepository fileRepository;
    private final UserRepository userRepository;


    @Autowired
    public FileService(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initStorage() {
        try {
            Files.createDirectories(Paths.get(storageLocation));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!", e);
        }
    }

    @Transactional
    public void uploadFile(String login, MultipartFile file) {
        AppUser user = getUserByLogin(login);

        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty()) {
            throw new RuntimeException("Filename is invalid");
        }

        Path userDir = Paths.get(storageLocation, login);
        try {
            Files.createDirectories(userDir); // Ensure user directory exists
            Path destination = userDir.resolve(filename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            FileEntity entity = new FileEntity();
            entity.setFilename(filename);
            entity.setSize(file.getSize());
            entity.setPath(destination.toString());
            entity.setUser(user);

            fileRepository.save(entity);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Transactional(readOnly = true)
    public byte[] downloadFile(String login, String filename) throws IOException {
        AppUser user = userRepository.findByLogin(login).orElseThrow();
        FileEntity file = fileRepository.findByFilenameAndUser(filename, user);
        if (file == null) {
            throw new RuntimeException("File not found");
        }

        return Files.readAllBytes(Paths.get(file.getPath()));
    }

    @Transactional
    public void deleteFile(String login, String filename) {
        FileEntity entity = getFileEntity(login, filename);
        try {
            Files.deleteIfExists(Paths.get(entity.getPath()));
            fileRepository.delete(entity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    @Transactional
    public void renameFile(String login, String oldName, String newName) throws IOException {
        AppUser user = userRepository.findByLogin(login).orElseThrow();
        FileEntity file = fileRepository.findByFilenameAndUser(oldName, user);
        if (file == null) {
            throw new RuntimeException("File not found");
        }

        Path oldPath = Paths.get(file.getPath());
        Path newPath = oldPath.resolveSibling(newName);
        Files.move(oldPath, newPath);

        file.setFilename(newName);
        file.setPath(newPath.toString());
        fileRepository.save(file);
    }

    @Transactional(readOnly = true)
    public List<FileResponse> listFiles(String login) {
        AppUser user = getUserByLogin(login);
        return fileRepository.findByUser(user).stream()
                .map(file -> new FileResponse(file.getFilename(), file.getSize()))
                .collect(Collectors.toList());
    }

    private AppUser getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found: " + login));
    }

    private FileEntity getFileEntity(String login, String filename) {
        AppUser user = getUserByLogin(login);
        return Optional.ofNullable(fileRepository.findByFilenameAndUser(filename, user))
                .orElseThrow(() -> new RuntimeException("File not found: " + filename));
    }
}