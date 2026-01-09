package org.example.pbases.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Animal_Reptil")
public class AnimalReptil extends Animal {

    private Boolean venenoso;
}
