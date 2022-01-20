package com.Adam.Lucja.JavaPRO.DTO.Request;

import com.Adam.Lucja.JavaPRO.Entity.Temat;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjektRequest {
    private Timestamp submissionDate;
    private Timestamp deadline;
    private Double mark;
    private Long tematId;
    private Long studentId;
}
