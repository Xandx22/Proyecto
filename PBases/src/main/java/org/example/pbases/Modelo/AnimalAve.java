package org.example.pbases.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Animal_Ave")
public class AnimalAve extends Animal {

    private Double envergadura;
    private Boolean vuelo;
}