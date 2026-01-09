package org.example.pbases.Repositorios;

import org.example.pbases.Modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
}

