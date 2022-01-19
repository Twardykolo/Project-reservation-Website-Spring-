package com.Adam.Lucja.JavaPRO.DTO.Response;

import com.Adam.Lucja.JavaPRO.Entity.Temat;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TematResponse {
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
}
