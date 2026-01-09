package org.example.pbases.Controlador;

import org.example.pbases.DTO.LoginRequest;
import org.example.pbases.Modelo.Usuario;
import org.example.pbases.Repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RepositorioUsuario repositorio;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Validación básica
        if (request.getCorreo() == null || request.getContrasena() == null) {
            return ResponseEntity.badRequest().body("{\"status\":\"error\",\"mensaje\":\"Correo o contraseña faltante\"}");
        }

        Optional<Usuario> usuarioOptional = repositorio.findByCorreo(request.getCorreo());

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(401).body("{\"status\":\"error\",\"mensaje\":\"Usuario no encontrado\"}");
        }

        Usuario usuario = usuarioOptional.get();

        // Comparación directa (texto plano, solo para práctica escolar)
        if (!usuario.getContrasena().equals(request.getContrasena())) {
            return ResponseEntity.status(401).body("{\"status\":\"error\",\"mensaje\":\"Credenciales inválidas\"}");
        }

        // Éxito
        String respuesta = String.format("{\"status\":\"ok\",\"usuario\":\"%s\",\"rol\":\"%s\"}",
                usuario.getCorreo(), usuario.getRol());

        return ResponseEntity.ok(respuesta);
    }}