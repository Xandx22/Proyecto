package org.example.pbases.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Animal_Anfibio")
public class AnimalAnfibio extends Animal {

    private String habitatNatal;
}