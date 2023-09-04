package com.example.jelog.web;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileDownloadController {

    private final String UPLOAD_DIR = "uploads/";

    @GetMapping("api/images/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename){
        try {
            Path file = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            // Get MIME type dynamically (basic example)
            String mimeType = "image/png"; // 기본값으로 png 설정
            if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                mimeType = "image/jpeg";
            } else if (filename.endsWith(".gif")) {
                mimeType = "image/gif";
            }

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
