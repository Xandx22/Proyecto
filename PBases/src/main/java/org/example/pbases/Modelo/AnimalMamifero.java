package org.example.pbases.Modelo;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Animal_Mamifero")
public class AnimalMamifero extends Animal {

    private Integer gestacion;
    private Integer criasPromedio;
}