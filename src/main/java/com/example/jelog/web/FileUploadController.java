package com.example.jelog.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
public class FileUploadController {

    private final String UPLOAD_DIR = "uploads/";
    private final String ACCESS_PATH = "http://118.67.132.220:8080/api/images/";

    @PostMapping("/api/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                boolean dirCreated = directory.mkdirs();
                if(!dirCreated){
                    throw new Exception("Failed to create directory:: " + directory.getAbsolutePath());
                }
            }

            System.out.println("UPLOAD DIRECTORY: " + directory.getAbsolutePath()); // 절대 경로 출력


            String fileName = file.getOriginalFilename();
            System.out.println("fileName = " + fileName);
            String filePath = directory.getAbsolutePath() + fileName;
            System.out.println("FILEPATH ::: " + filePath);
            File dest = new File(filePath);
            file.transferTo(dest);

            return ResponseEntity.ok().body(ACCESS_PATH+fileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }
}
