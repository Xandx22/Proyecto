package org.example.pbases.Controlador;

import org.example.pbases.DTO.LoginRequest;
import org.example.pbases.Modelo.Usuario;
import org.example.pbases.Repositorios.RepositorioUsuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final RepositorioUsuario repo;
    private final PasswordEncoder encoder;

    public UsuarioController(RepositorioUsuario repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    // 🔐 LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getCorreo() == null || request.getContrasena() == null) {
            System.out.println("Error: correo o contraseña faltante en la petición");
            return ResponseEntity.badRequest().body("{\"status\":\"error\",\"mensaje\":\"Correo o contraseña faltante\"}");
        }

        System.out.println("Intentando iniciar sesion con: " + request.getCorreo());

        Optional<Usuario> usuarioOptional = repo.findByCorreo(request.getCorreo());
        //System.out.println("Resultado de búsqueda en repositorio: " + usuarioOptional);

        if (usuarioOptional.isEmpty()) {
            System.out.println("Usuario no encontrado en la base de datos");
            return ResponseEntity.status(401).body("{\"status\":\"error\",\"mensaje\":\"Usuario no encontrado\"}");
        }

        Usuario usuario = usuarioOptional.get();
        System.out.println("Usuario encontrado: " + usuario.getCorreo() + " | Rol: " + usuario.getRol());

        if (!encoder.matches(request.getContrasena(), usuario.getContrasena())) {
            System.out.println("Contraseña incorrecta para el usuario: " + usuario.getCorreo());
            return ResponseEntity.status(401).body("{\"status\":\"error\",\"mensaje\":\"Credenciales inválidas\"}");
        }

        String respuesta = String.format("{\"status\":\"ok\",\"usuario\":\"%s\",\"rol\":\"%s\"}",
                usuario.getCorreo(), usuario.getRol());

        System.out.println("Login exitoso para: " + usuario.getCorreo());
        return ResponseEntity.ok(respuesta);
    }

    // 📝 CREAR USUARIO
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        System.out.println("Creando usuario con correo: " + usuario.getCorreo());
        System.out.println("Nombre recibido: " + usuario.getNombre());
        System.out.println("Rol recibido: " + usuario.getRol());

        if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()
                || usuario.getContrasena() == null || usuario.getContrasena().isBlank()) {
            System.out.println("Datos inválidos: correo o contraseña vacíos");
            return ResponseEntity.badRequest().body(
                    java.util.Map.of(
                            "status", "error",
                            "mensaje", "Correo y contraseña son obligatorios"
                    )
            );
        }

        Optional<Usuario> existente = repo.findByCorreo(usuario.getCorreo());
        if (existente.isPresent()) {
            System.out.println("Correo ya registrado: " + usuario.getCorreo());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    java.util.Map.of(
                            "status", "error",
                            "mensaje", "El correo ya está registrado"
                    )
            );
        }

        String raw = usuario.getContrasena();
        System.out.println(" Contraseña guardada: ");
        usuario.setContrasena(encoder.encode(raw));

        Usuario guardado = repo.save(usuario);
        System.out.println("Usuario guardado en BD: " + guardado.getCorreo() + " | Rol: " + guardado.getRol());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                java.util.Map.of(
                        "status", "ok",
                        "usuario", guardado.getCorreo(),
                        "rol", guardado.getRol()
                )
        );
    }
}