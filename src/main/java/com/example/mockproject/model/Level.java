package com.example.mockproject.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Level extends Audit<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer levelId;

    private String name ;

    public Level(String name){
        this.name = name;
    }
}
