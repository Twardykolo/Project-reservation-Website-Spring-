package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Request.FileRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.FileResponse;
import com.Adam.Lucja.JavaPRO.Entity.File;
import com.Adam.Lucja.JavaPRO.Repository.FileRepository;
import lombok.SneakyThrows;
import org.apache.catalina.webresources.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.FileEditor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
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
    @SneakyThrows
    @Transactional
    public FileResponse updateFile (MultipartFile plik, String id){
        File file = fileRepository.getById(id);
        file.setName(plik.getOriginalFilename());
        file.setType(plik.getContentType());
        file.setData(plik.getBytes());

        File savedFile = fileRepository.save(file);
        return new FileResponse(savedFile);
    }
    public void deleteFile(String id){
        File file = fileRepository.getById(id);
        fileRepository.delete(file);
    }
}
