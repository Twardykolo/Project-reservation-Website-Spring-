package com.Adam.Lucja.JavaPRO.DTO.Response;

import com.Adam.Lucja.JavaPRO.Entity.Temat;
import lombok.*;

import java.sql.Timestamp;
import java.util.Comparator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TematResponse implements Comparable<TematResponse> {
    private Long id;
    private String name;
    private String description;
    private Boolean isReserved;
    private Integer liczbaOsob;
    private Timestamp deadline;

    public TematResponse(Temat temat) {
        this.id=temat.getId();
        this.description= temat.getDescription();
        this.isReserved=temat.getIsReserved();
        this.name= temat.getName();
        this.deadline = temat.getDeadline();
    }

    public int compareTo(TematResponse tematResponse2){
        return this.id.compareTo(tematResponse2.getId());
    }
}
