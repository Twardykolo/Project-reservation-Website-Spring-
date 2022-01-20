package com.Adam.Lucja.JavaPRO.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(schema="projekty" ,name="projekty_student")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Projekt2Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true, nullable = false)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name="projektId", nullable = false)
    private Projekt projekt;
    @ManyToOne(optional = false)
    @JoinColumn(name="studentId", nullable = false)
    private Student student;
}
