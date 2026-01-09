package org.example.pbases.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.pbases.Modelo.Animal;

public interface RepositorioAnimal extends JpaRepository<Animal, Integer> {
}