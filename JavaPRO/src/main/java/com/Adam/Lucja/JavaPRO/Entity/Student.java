package com.Adam.Lucja.JavaPRO.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema="projekty" ,name="studenci")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
//MODEL
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String nrAlbum;
    @OneToMany(mappedBy = "student")
    private List<Projekt2Student> projekt2student;
    @OneToOne
    private Login login;
}
