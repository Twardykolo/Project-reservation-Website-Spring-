package com.Adam.Lucja.JavaPRO.Entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(schema="projekty" ,name="tematy")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Temat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true, nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false,length = 1000)
    private String description;
    @Column(nullable = false)
    private Boolean isReserved;
    @Column(nullable = false)
    private Timestamp deadline;
}
