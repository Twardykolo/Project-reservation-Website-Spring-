package com.Adam.Lucja.JavaPRO.Entity;

import lombok.*;

import javax.persistence.*;

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
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Boolean isReserved;
}
