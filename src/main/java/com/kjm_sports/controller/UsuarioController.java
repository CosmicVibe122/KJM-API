package com.kjm_sports.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional; // 🔑 Necesario para buscar por email

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; // Para futuras eliminaciones
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // Para futuras ediciones
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kjm_sports.model.Usuario;
import com.kjm_sports.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. GET: Ver todos los usuarios
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // 2. POST: Crear nuevo usuario (Registro)
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("USER"); 
        }
        // 🚨 ADVERTENCIA: Guarda la contraseña en texto plano
        return usuarioRepository.save(usuario); 
    }
    
    // 3. GET: Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // --- 4. ENDPOINT DE LOGIN (Comparación de texto plano) ---
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String password = credenciales.get("password");

        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email); 

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            
            if (usuario.getPassword() != null && usuario.getPassword().equals(password)) {
                
                usuario.setPassword(null); // Ocultar la contraseña antes de devolver
                return ResponseEntity.ok(usuario); // 200 OK: Login Exitoso
            }
        }
        
        // Error 401: No autorizado (credenciales incorrectas o contraseña NULL)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
    }
    // 5. PUT y DELETE
    
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario detalles) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            Usuario userExistente = usuarioOptional.get();
            
            // Lógica de actualización (sin actualizar contraseña si viene vacía)
            userExistente.setNombre(detalles.getNombre());
            userExistente.setEmail(detalles.getEmail());
            userExistente.setDireccion(detalles.getDireccion());
            userExistente.setTelefono(detalles.getTelefono());
            userExistente.setRol(detalles.getRol());

            // Solo actualiza la contraseña si se proporciona una nueva
            if (detalles.getPassword() != null && !detalles.getPassword().isEmpty()) {
                userExistente.setPassword(detalles.getPassword());
            }

            userExistente.setPassword(null); // Ocultar antes de responder
            return ResponseEntity.ok(usuarioRepository.save(userExistente));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok().build(); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }
}