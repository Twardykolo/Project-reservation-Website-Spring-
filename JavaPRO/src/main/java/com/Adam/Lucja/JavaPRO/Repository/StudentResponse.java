package com.Adam.Lucja.JavaPRO.Repository;

import com.Adam.Lucja.JavaPRO.Entity.Student;
import lombok.*;

import javax.persistence.Column;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
    private Long id;
    private String name;
    private String surname;
    private String password;
    private String email;
    private String nrAlbum;

    public StudentResponse(Student student){
        this.id=student.getId();
        this.name=student.getName();
        this.surname=student.getSurname();
        this.password=student.getPassword();
        this.email=student.getEmail();
        this.nrAlbum=student.getNrAlbum();
    }
}

