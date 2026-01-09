package org.example.pbases.Modelo;

import org.example.pbases.Modelo.enums.TipoHabitat;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Habitat")
public class Habitat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHabitat;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private TipoHabitat tipo;

    private Integer capacidadMax;
    private String ubicacion;

    @OneToMany(mappedBy = "habitat")
    private List<Animal> animales;
}
