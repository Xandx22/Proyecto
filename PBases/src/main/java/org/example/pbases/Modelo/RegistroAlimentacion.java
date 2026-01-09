package org.example.pbases.Modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.io.Serializable;

@Entity
@Table(name = "RegistroAlimentacion")
@IdClass(RegistroAlimentacion.class)
public class RegistroAlimentacion {

    @Id
    private Integer idAnimal;

    @Id
    private Integer idAlimento;

    @Id
    private LocalDate fecha;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "id_cuidador")
    private Cuidador cuidador;
}