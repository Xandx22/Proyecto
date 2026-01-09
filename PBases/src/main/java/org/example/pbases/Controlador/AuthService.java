package org.example.pbases.Controlador;

import org.example.pbases.Modelo.Usuario;
import org.example.pbases.Repositorios.RepositorioUsuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final RepositorioUsuario repo;
    private final PasswordEncoder encoder;

    public AuthService(RepositorioUsuario repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public Usuario login(String correo, String contrasena) {
        return repo.findByCorreo(correo)
                .filter(u -> encoder.matches(contrasena, u.getContrasena()))
                .orElse(null);
    }
}