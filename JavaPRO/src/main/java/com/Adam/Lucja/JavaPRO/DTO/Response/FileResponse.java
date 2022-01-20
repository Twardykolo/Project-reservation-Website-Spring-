package com.Adam.Lucja.JavaPRO.DTO.Response;

import com.Adam.Lucja.JavaPRO.Entity.File;
import com.Adam.Lucja.JavaPRO.Entity.Projekt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FileResponse {
    private String name;
    private String url;
    private String type;
    private long size;

    public FileResponse(File file){
        this.name=file.getName();
        this.type= file.getType();
        this.size=file.getData().length;
    }
}
