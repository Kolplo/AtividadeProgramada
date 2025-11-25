package com.atividadeProgramada.AtividadeProgramada2.controller;

import com.atividadeProgramada.AtividadeProgramada2.entity.Usuario;
import com.atividadeProgramada.AtividadeProgramada2.security.JwtUtil;
import com.atividadeProgramada.AtividadeProgramada2.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String nome = request.get("nome");
        String email = request.get("email");
        String senha = request.get("senha");

        if (email == null || senha == null) {
            return ResponseEntity.badRequest().body("email e senha são obrigatórios");
        }

        // verifica se já existe
        if (usuarioService.buscarPorEmail(email).isPresent()) {
            return ResponseEntity.status(409).body("Usuário já cadastrado com esse e-mail");
        }

        Usuario usuario = usuarioService.registrarUsuario(nome, email, senha);
        // retire informações sensíveis antes de retornar (ex.: senha)
        usuario.setPassword(null);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String senha = request.get("senha");

        if (email == null || senha == null) {
            return ResponseEntity.badRequest().body("email e senha são obrigatórios");
        }

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(senha, usuario.getPassword())) {
                String token = JwtUtil.generateToken(usuario.getEmail());
                return ResponseEntity.ok(Map.of("token", token));
            }
        }
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}
