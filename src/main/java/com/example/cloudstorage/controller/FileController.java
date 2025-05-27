package com.example.cloudstorage.controller;

import com.example.cloudstorage.dto.FileResponse;
import com.example.cloudstorage.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<Void> upload(@RequestHeader("auth-token") String token,
                                       @RequestParam("file") MultipartFile file) throws IOException {
        fileService.uploadFile(token, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<byte[]> download(@RequestHeader("auth-token") String token,
                                           @RequestParam("filename") String filename) throws IOException {
        byte[] data = fileService.downloadFile(token, filename);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(data);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestHeader("auth-token") String token,
                                       @RequestParam("filename") String filename) throws IOException {
        fileService.deleteFile(token, filename);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> rename(@RequestHeader("auth-token") String token,
                                       @RequestParam("filename") String oldName,
                                       @RequestParam("newName") String newName) throws IOException {
        fileService.renameFile(token, oldName, newName);
        return ResponseEntity.ok().build();
    }

}