package com.jeonsaeyukjun.jeonsaeyukjunbe.S3.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.S3.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

@Slf4j
@RestController
@RequestMapping("/file")
public class S3FileUploadController {

    private final S3Service s3Service;

    @Autowired
    public S3FileUploadController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        // S3에 파일 업로드 후 파일명을 MySQL에 저장
        String fileName = s3Service.uploadFileAndSaveToDb(file);
        return ResponseEntity.ok(fileName);
    }

    @GetMapping("/download/{contractName}")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable String contractName) throws MalformedURLException {
        // 파일 다운로드 로직
        try{
            UrlResource resource = s3Service.downloadFile(contractName);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }
}

