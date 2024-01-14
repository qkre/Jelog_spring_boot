package com.example.jelog.web.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/image")
public class ImageApiController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.access-path}")
    private String accessPath;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file){
        try{
            File directory = new File(uploadDir);
            if(!directory.exists()){
                directory.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            String filePath = directory.getAbsolutePath() + "/" + fileName;

            File destination = new File(filePath);
            file.transferTo(destination);

            Map<String, String> response = new HashMap<>();

            response.put("accessPath", accessPath);
            response.put("fileName", fileName);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new HashMap<>());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadImage(@RequestParam String fileName){
        try{
            Path file = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());

            String mimeType = "image/png";
            if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                mimeType = "image/jpeg";
            } else if(fileName.endsWith(".gif")){
                mimeType = "image/gif";
            }

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(resource);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
