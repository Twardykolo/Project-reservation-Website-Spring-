package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.Entity.File;
import com.Adam.Lucja.JavaPRO.Repository.FileRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    public File saveFile(MultipartFile plik) throws IOException{
        String fileName = StringUtils.cleanPath(plik.getOriginalFilename());
        File file = new File(fileName, plik.getContentType(), plik.getBytes());
        return fileRepository.save(file);
    }
    public File getFile(String id){
        return fileRepository.findById(id).get();
    }
    public Stream<File> getAllFiles(){
        return fileRepository.findAll().stream();
    }
}
