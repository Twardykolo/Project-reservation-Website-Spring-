package com.Adam.Lucja.JavaPRO.DTO.Request;

import lombok.*;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TematRequest {
    private String name;
    private String description;
    private Optional<Boolean> isReserved;
}
