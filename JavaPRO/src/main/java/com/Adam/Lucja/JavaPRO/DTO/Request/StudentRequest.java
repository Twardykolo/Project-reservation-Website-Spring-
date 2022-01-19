package com.Adam.Lucja.JavaPRO.DTO.Request;

import lombok.*;

import javax.persistence.Column;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StudentRequest {
    private String name;
    private String surname;
    private String password;
    private String email;
    private String nrAlbum;
}
