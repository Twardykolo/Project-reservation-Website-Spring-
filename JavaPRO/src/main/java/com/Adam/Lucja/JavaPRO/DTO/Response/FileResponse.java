package com.Adam.Lucja.JavaPRO.DTO.Response;

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
}
