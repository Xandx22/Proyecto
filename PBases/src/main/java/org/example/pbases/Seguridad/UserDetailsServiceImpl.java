package org.example.pbases.Seguridad;

import org.example.pbases.Modelo.Usuario;
import org.example.pbases.Repositorios.RepositorioUsuario;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RepositorioUsuario usuarioRepository;

    public UserDetailsServiceImpl(RepositorioUsuario usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado"));

        return new User(
                usuario.getCorreo(),      // username
                usuario.getContrasena(),  // password (BCrypt)
                List.of(new SimpleGrantedAuthority(usuario.getRol()))
        );
    }
}
