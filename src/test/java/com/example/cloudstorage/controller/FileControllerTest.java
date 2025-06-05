package com.example.cloudstorage.controller;

import com.example.cloudstorage.controller.FileController;
import com.example.cloudstorage.dto.FileResponse;
import com.example.cloudstorage.service.FileService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FileController.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    public void testListFiles() throws Exception {
        List<FileResponse> mockFiles = Arrays.asList(
                new FileResponse("file1.txt", 123L),
                new FileResponse("file2.txt", 456L)
        );
        Mockito.when(fileService.listFiles("test-token")).thenReturn(mockFiles);

        mockMvc.perform(get("/list")
                        .header("auth-token", "test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].filename").value("file1.txt"))
                .andExpect(jsonPath("$[1].filename").value("file2.txt"));
    }
}