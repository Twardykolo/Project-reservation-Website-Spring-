package com.Adam.Lucja.JavaPRO.DTO.Response;

import com.Adam.Lucja.JavaPRO.Entity.Projekt;
import com.Adam.Lucja.JavaPRO.Entity.Temat;
import lombok.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjektResponse {
    private Long id;
    private String submissionDate;
    private String deadline;
    private Double mark;
    private Temat temat;
    private String fileUrl;
    private String name;
    private List<StudentResponse> studenci;

    public ProjektResponse(Projekt projekt){
        this.id= projekt.getId();
        if(projekt.getSubmissionDate()!=null)
            this.submissionDate=new SimpleDateFormat("dd-MM-yyyy").format(projekt.getSubmissionDate());
        if(projekt.getDeadline()!=null)
            this.deadline=new SimpleDateFormat("dd-MM-yyyy").format(projekt.getDeadline());
        this.mark= projekt.getMark();
        this.temat=projekt.getTemat();
        this.name=this.temat.getName();
        if(projekt.getFile()!=null)
            this.fileUrl=ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/files/")
                    .path(projekt.getFile().getId()).toUriString();
    }
}
