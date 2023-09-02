package pet.objectstorage.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pet.objectstorage.service.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("files") MultipartFile[] files,@RequestParam(value="path",required = false) String path) {
        try {
            storageService.uploadFile(files,path);
            return ResponseEntity.ok("Image uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed.");
        }
    }

    @GetMapping("/{bucketName}/{fileName}")
    public ResponseEntity<byte[]> getImageFromBucket( @PathVariable String bucketName, @PathVariable String fileName) {
        return getImageFromDisk(bucketName+"/"+fileName);
    }
    private ResponseEntity<byte[]> getImageFromDisk(String fileName) {
        try {
            byte[] imageData = storageService.getFile(fileName);
            Path filePath = Paths.get(fileName);
            return ResponseEntity.ok().contentType(MediaType.valueOf(Files.probeContentType(filePath))).body(imageData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getImage( @PathVariable String fileName) {
        return getImageFromDisk(fileName);
    }
}