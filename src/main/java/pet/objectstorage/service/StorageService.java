package pet.objectstorage.service;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${upload.directory}")
    private String uploadDirectory;

    public void uploadFile(MultipartFile[] files,String path) throws IOException {
        var newPath=uploadDirectory;
        if (path!=null)
            newPath=uploadDirectory+"\\"+path;
        Path folder = Paths.get(newPath);
        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(folder); // Creates the folder and any missing parent folders
                System.out.println("Folder created successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error creating folder.");
            }
        } else {
            System.out.println(newPath+" : Folder already exists");
        }
        for (MultipartFile file:files){
            Path filePath = Paths.get(newPath, file.getOriginalFilename());
            Files.write(filePath, file.getBytes());
            System.out.println(filePath+" : File uploaded");
        }
    }

    public byte[] getFile(String filename) throws IOException {
        Path filePath = Paths.get(uploadDirectory, filename);
        return Files.readAllBytes(filePath);
    }
}