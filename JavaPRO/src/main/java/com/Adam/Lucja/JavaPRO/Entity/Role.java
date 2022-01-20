package com.Adam.Lucja.JavaPRO.Entity;

import com.Adam.Lucja.JavaPRO.Util.ERole;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(schema="projekty")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column
    private ERole name;

    @Override
    public String toString(){
        return name.toString();
    }
}
