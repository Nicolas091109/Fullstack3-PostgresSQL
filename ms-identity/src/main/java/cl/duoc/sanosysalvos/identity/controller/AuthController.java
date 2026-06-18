package cl.duoc.sanosysalvos.identity.controller;

import cl.duoc.sanosysalvos.identity.model.Usuario;
import cl.duoc.sanosysalvos.identity.repository.UsuarioRepository;
import cl.duoc.sanosysalvos.identity.security.JwUtils;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro, login y listado de usuarios")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwUtils jwtUtils;

    @Autowired
    public AuthController(UsuarioRepository usuarioRepository,
                          AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder,
                          JwUtils jwtUtils) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/usuarios")
    @Operation(summary = "Listar usuarios", description = "Obtiene la lista completa de usuarios registrados. Requiere autenticación por Bearer Token.")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar un usuario", description = "Registra un nuevo usuario en el sistema. Todos los registros web inician con rol 'USER' por seguridad.")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {
        prepararUsuarioParaRegistro(usuario);
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @PostMapping("/usuarios")
    @Operation(summary = "Crear un usuario (Admin/Alternativo)", description = "Creación alternativa de usuarios. Al igual que el registro público, le asigna el rol 'USER'.")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        prepararUsuarioParaRegistro(usuario);
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario con su email y contraseña, retornando un token JWT.")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Usuario credenciales) {
        if (credenciales.getEmail() == null || credenciales.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email es obligatorio.");
        }

        if (credenciales.getPassword() == null || credenciales.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credenciales.getEmail(), credenciales.getPassword())
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas.");
        }

        Usuario usuario = usuarioRepository.findByEmail(credenciales.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas."));

        String token = jwtUtils.generateToken(usuario.getEmail());

        Map<String, Object> response = Map.of(
                "message", "Inicio de sesión exitoso.",
                "token", token,
                "usuario", Map.of(
                        "id", usuario.getId(),
                        "nombre", usuario.getNombre(),
                        "email", usuario.getEmail(),
                        "rol", usuario.getRol()));

        return ResponseEntity.ok(response);
    }

    private void prepararUsuarioParaRegistro(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email es obligatorio.");
        }

        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria.");
        }

        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio.");
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario registrado con ese email.");
        }

        // Por seguridad y requerimiento del usuario, todos los registros nuevos inician como USER.
        // Los privilegios de ADMIN se asignan exclusivamente de manera directa en la Base de Datos PostgreSQL.
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol("USER");
    }
}
