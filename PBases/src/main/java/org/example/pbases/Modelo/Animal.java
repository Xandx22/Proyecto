package org.example.pbases.Modelo;



import jakarta.persistence.*;
import java.time.LocalDate;
import org.example.pbases.Modelo.enums.Sexo;

@Entity
@Table(name = "Animal")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAnimal;

    private String nombre;
    private String especie;
    private Integer edad;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private LocalDate fechaLlegada;
    private String estadoSalud;

    @ManyToOne
    @JoinColumn(name = "id_habitat")
    private Habitat habitat;
}
