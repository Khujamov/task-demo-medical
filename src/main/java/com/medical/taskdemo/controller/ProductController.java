package com.medical.taskdemo.controller;

import com.medical.taskdemo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping("/upload-csv")
    public ResponseEntity<?> uploadCsv(@RequestParam(name = "file") MultipartFile file) throws Exception {
        service.uploadFile(file);
        return ResponseEntity.ok(service.getAllAsTree());
    }
}
