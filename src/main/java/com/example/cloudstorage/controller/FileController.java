package com.example.cloudstorage.controller;

import com.example.cloudstorage.dto.FileResponse;
import com.example.cloudstorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // Загрузка файла
    @PostMapping("/file")
    public ResponseEntity<Void> uploadFile(@RequestHeader("auth-token") String token,
                                           @RequestParam("file") MultipartFile file) throws IOException {
        fileService.uploadFile(token, file);
        return ResponseEntity.ok().build();
    }

    // Список файлов
    @GetMapping("/list")
    public ResponseEntity<List<FileResponse>> listFiles(@RequestHeader("auth-token") String token) {
        return ResponseEntity.ok(fileService.listFiles(token));
    }

    // Удаление файла
    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@RequestHeader("auth-token") String token,
                                           @RequestParam("filename") String filename) throws IOException {
        fileService.deleteFile(token, filename);
        return ResponseEntity.ok().build();
    }

    // Переименование файла
    @PutMapping("/file")
    public ResponseEntity<Void> renameFile(@RequestHeader("auth-token") String token,
                                           @RequestParam("filename") String oldFilename,
                                           @RequestBody String newFilename) throws IOException {
        fileService.renameFile(token, oldFilename, newFilename.replace("\"", ""));
        return ResponseEntity.ok().build();
    }
}
