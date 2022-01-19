package com.Adam.Lucja.JavaPRO.Controller;

import com.Adam.Lucja.JavaPRO.DTO.Response.FileResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.MessageResponse;
import com.Adam.Lucja.JavaPRO.Entity.File;
import com.Adam.Lucja.JavaPRO.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/")
    public ResponseEntity<MessageResponse> uploadFile(@RequestParam("file") MultipartFile file){
        String message = "";
        try {
            fileService.saveFile(file);
            message = "Pomyślnie zapisano plik: "+file.getOriginalFilename();
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (IOException e){
            message = "Nie udało się załadować pliku\n"+e.getMessage();
            return ResponseEntity.internalServerError().body(new MessageResponse(message));
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<FileResponse>> getAllFilesList(){
        List<FileResponse> fileList = fileService.getAllFiles().map(
                file -> {
                    String fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath().path("/files/")
                            .path(file.getId()).toUriString();
                    return FileResponse.builder()
                            .name(file.getName())
                            .type(file.getType())
                            .size(file.getData().length)
                            .url(fileDownloadUri).build();
                }).collect(Collectors.toList());
        return ResponseEntity.ok(fileList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id){
        File file = fileService.getFile(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getName()+"\"").body(file.getData());
    }

}
