package com.Adam.Lucja.JavaPRO.DTO.Response;

import com.Adam.Lucja.JavaPRO.Entity.Student;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String nrAlbum;

    public StudentResponse(Student student){
        this.id=student.getId();
        this.name=student.getName();
        this.surname=student.getSurname();
        this.email=student.getEmail();
        this.nrAlbum=student.getNrAlbum();
    }
}

