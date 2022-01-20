package com.Adam.Lucja.JavaPRO.DTO.Response;

import com.Adam.Lucja.JavaPRO.Entity.File;
import com.Adam.Lucja.JavaPRO.Entity.Temat;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjektResponse {
    private Long id;
    private Timestamp submissionDate;
    private Timestamp deadline;
    private Double mark;
    private Temat temat;

}
