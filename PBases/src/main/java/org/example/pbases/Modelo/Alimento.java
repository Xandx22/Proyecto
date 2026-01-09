package org.example.pbases.Modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Alimento")
public class Alimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAlimento;

    private String nombre;
    private String tipo;
    private Integer cantidadDisponible;
    private LocalDate fechaCaducidad;
}