package com.Adam.Lucja.JavaPRO.DTO.Response;

import com.Adam.Lucja.JavaPRO.Entity.Projekt;
import com.Adam.Lucja.JavaPRO.Entity.Temat;
import lombok.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.sql.Timestamp;
import java.util.Optional;

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
    private String fileUrl;
    private String name;

    public ProjektResponse(Projekt projekt){
        this.id= projekt.getId();
        this.submissionDate=projekt.getSubmissionDate();
        this.deadline=projekt.getDeadline();
        this.mark= projekt.getMark();
        this.temat=projekt.getTemat();
        this.name=this.temat.getName();
        if(projekt.getFile()!=null)
            this.fileUrl=ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/files/")
                    .path(projekt.getFile().getId()).toUriString();
    }
}
