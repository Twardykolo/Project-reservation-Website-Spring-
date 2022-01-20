package com.Adam.Lucja.JavaPRO.DTO.Response;

import com.Adam.Lucja.JavaPRO.Entity.File;
import com.Adam.Lucja.JavaPRO.Entity.Projekt;
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

    public ProjektResponse(Projekt projekt){
        this.id= projekt.getId();
        this.submissionDate=projekt.getSubmissionDate();
        this.deadline=projekt.getDeadline();
        this.mark= projekt.getMark();
        this.temat=projekt.getTemat();
    }
}
