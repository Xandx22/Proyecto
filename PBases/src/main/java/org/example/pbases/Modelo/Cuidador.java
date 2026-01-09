package org.example.pbases.Modelo;

import jakarta.persistence.*;
import org.example.pbases.Modelo.enums.Especialidad;

@Entity
@Table(name = "Cuidador")
public class Cuidador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCuidador;

    private String nombre;
    private Integer edad;

    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;

    private String contacto;
}