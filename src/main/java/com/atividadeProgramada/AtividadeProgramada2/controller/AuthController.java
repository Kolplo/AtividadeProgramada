package com.atividadeProgramada.AtividadeProgramada2.controller;

import com.atividadeProgramada.AtividadeProgramada2.dto.User.LoginUserRequestDTO;
import com.atividadeProgramada.AtividadeProgramada2.dto.User.RegisterUserRequestDTO;
import com.atividadeProgramada.AtividadeProgramada2.entity.Usuario;
import com.atividadeProgramada.AtividadeProgramada2.repository.UsuarioRepository;
import com.atividadeProgramada.AtividadeProgramada2.infra.security.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @GetMapping
    public String authPage() {
        return "auth";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setPassword(passwordEncoder.encode(dto.senha()));
        usuarioRepository.save(usuario);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String registerForm(@org.springframework.web.bind.annotation.RequestParam String nome,
                               @org.springframework.web.bind.annotation.RequestParam String email,
                               @org.springframework.web.bind.annotation.RequestParam String senha,
                               HttpServletResponse response) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            return "redirect:/auth?error=emailexists";
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(senha));
        usuarioRepository.save(usuario);

        // auto-login: generate token and set cookie
        String token = tokenService.generateToken(usuario);
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/aluno";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserRequestDTO dto, HttpServletResponse response) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
            authenticationManager.authenticate(authToken);

            Usuario usuario = usuarioRepository.findByEmail(dto.email()).orElseThrow();
            String token = tokenService.generateToken(usuario);

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            // optionally set secure and maxAge
            response.addCookie(cookie);

            return ResponseEntity.ok().build();
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String loginForm(@org.springframework.web.bind.annotation.RequestParam String email,
                            @org.springframework.web.bind.annotation.RequestParam String senha,
                            HttpServletResponse response) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(email, senha);
            authenticationManager.authenticate(authToken);

            Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
            String token = tokenService.generateToken(usuario);

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/aluno";
        } catch (AuthenticationException ex) {
            return "redirect:/auth?error=invalid";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/auth";
    }
}
