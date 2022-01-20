package com.Adam.Lucja.JavaPRO.Entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(schema="projekty" ,name="projekty")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Projekt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private Timestamp submissionDate;
    @Column(nullable = false)
    private Timestamp deadline;
    private Double mark;
    @OneToMany(mappedBy = "projekt", fetch = FetchType.EAGER)
    private List<Projekt2Student> projekt2student;
    @OneToOne(optional = false)
    private File file;
    @OneToOne(optional = false)
    private Temat temat;
}