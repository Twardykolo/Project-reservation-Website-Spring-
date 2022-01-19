package com.Adam.Lucja.JavaPRO.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(schema="projekty" ,name="projekty")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Projekt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private Timestamp submissionDate;
    @Column(nullable = false)
    private Timestamp deadline;
    private Double mark;
}