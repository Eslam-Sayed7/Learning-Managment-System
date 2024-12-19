package com.Java.LMS.platform.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String pdfDir = "C:\\Users\\dell\\Documents\\GitHub\\Learning-Managment-System\\template\\pdf";
    private final String videoDir = "C:\\Users\\dell\\Documents\\GitHub\\Learning-Managment-System\\template\\video";

    // Method to save uploaded file
    public String saveFile(MultipartFile file, Long courseId, String lessonName) throws IOException {
        // Generate unique file name
        String beforeName = "courseId" + courseId + "-lessonName" + lessonName + "-";
        String originalFileName = beforeName + file.getOriginalFilename();
        String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";

        // Determine the file type and set the upload path accordingly
        Path uploadPath;
        if (extension.equalsIgnoreCase(".pdf")) {
            uploadPath = Paths.get(pdfDir);
        } else if (extension.equalsIgnoreCase(".mp4") || extension.equalsIgnoreCase(".avi") || extension.equalsIgnoreCase(".mkv")) {
            uploadPath = Paths.get(videoDir);
        } else {
            return null ;
        }

        // Create the directory if it doesn't exist
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Create the file in the directory
        Path filePath = uploadPath.resolve(originalFileName);
        file.transferTo(filePath);

        // Return the file's absolute path
        return filePath.toString();
    }
}
