package com.service.impls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.util.FileStorageProperties;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class FileStorageService {
	
    private final Path fileStorageLocation;
    
    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) throws Exception {
        fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            throw new Exception("Could not create the directory where the uploaded files will be stored.");
        }
    }
    
    public void storeFile(MultipartFile file, String fileName) throws Exception {
        try {
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new Exception("Could not store file " + fileName + ". Please try again!");
        }
    }
   
    public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            
            System.out.println("\n\n\n"+filePath.toString());
            
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new Exception("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new Exception("File not found " + fileName);
        }
    }
}