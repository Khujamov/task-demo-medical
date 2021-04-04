package com.medical.taskdemo.controller;

import com.medical.taskdemo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService service;

    @PostMapping("/upload-csv")
    public ResponseEntity<?> uploadCsv(@RequestParam(name = "file") MultipartFile file) throws Exception {
        service.uploadFile(file);
        return ResponseEntity.ok(service.getAllAsTree());
    }
}
