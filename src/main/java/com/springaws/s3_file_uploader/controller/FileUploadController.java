package com.springaws.s3_file_uploader.controller;

import com.springaws.s3_file_uploader.service.S3Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class FileUploadController {
    
    private final S3Service s3Service;
    
    public FileUploadController(S3Service s3Service) {
        this.s3Service = s3Service;
    }
    
    @GetMapping("/")
    public String showUploadForm() {
        return "upload";
    }
    
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
                return "redirect:/";
            }
            
            String fileUrl = s3Service.uploadFile(file);
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully");
            redirectAttributes.addFlashAttribute("fileUrl", fileUrl);
            
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Failed to upload file: " + e.getMessage());
        }
        
        return "redirect:/";
    }
}