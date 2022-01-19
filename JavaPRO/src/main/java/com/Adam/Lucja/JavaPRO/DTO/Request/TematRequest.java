package com.Adam.Lucja.JavaPRO.DTO.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TematRequest {
    private String name;
    private String description;
}
