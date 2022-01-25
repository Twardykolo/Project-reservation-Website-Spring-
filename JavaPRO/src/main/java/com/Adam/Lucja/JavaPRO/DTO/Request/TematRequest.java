package com.Adam.Lucja.JavaPRO.DTO.Request;

import lombok.*;
import org.apache.xalan.xsltc.trax.TemplatesImpl;

import java.sql.Timestamp;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TematRequest {
    private String name;
    private String description;
    private Timestamp deadline;
    private Optional<Boolean> isReserved;
}
