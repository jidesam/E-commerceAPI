package com.ecommerce.jideBrand.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
//        get the filename of the currentfile
        String currentFileName = file.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
//        generate a unique file name
        String filename = randomId.concat(currentFileName.substring(currentFileName.lastIndexOf(".")));
        String filePath = path + File.separator + filename;
//        check if path already exist and if not we can create a new one
        File folder  = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }
//        upload to the server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return filename;
    }
}
