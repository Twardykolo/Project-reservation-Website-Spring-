package com.Adam.Lucja.JavaPRO.DTO.Response;

import com.Adam.Lucja.JavaPRO.Entity.Temat;
import lombok.*;

import java.util.Comparator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TematResponse implements Comparator<TematResponse> {
    private Long id;
    private String name;
    private String description;
    private Boolean isReserved;

    public TematResponse(Temat temat) {
        this.id=temat.getId();
        this.description= temat.getDescription();
        this.isReserved=temat.getIsReserved();
        this.name= temat.getName();
    }
    public int compare(TematResponse temat1, TematResponse temat2){
        return temat1.getId().compareTo(temat2.getId());
    }
}
