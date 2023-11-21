package com.mdosys.file.controller;

import com.mdosys.file.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

@Controller
public class ComponentController {

    @Autowired
    private MinioService minioService;

    @PostMapping("/uploadFile")
    @ResponseBody
    public String uploadFile(MultipartFile file, String bucket) throws Exception {
        return minioService.uploadFiles(file, bucket);
    }

    @PostMapping("/deleteFile")
    @ResponseBody
    public void deleteFile(String path) throws Exception {
        minioService.deleteFile(path);
    }

    @GetMapping("/downloadFile")
    @ResponseBody
    public void download(String path, HttpServletResponse response) throws Exception{
        minioService.downloadComponent(path, response);
    }

    @GetMapping("/getInputStream")
    @ResponseBody
    public void getInputStream(String path, HttpServletResponse response) throws Exception{
        minioService.downloadComponent(path, response);
    }
}
