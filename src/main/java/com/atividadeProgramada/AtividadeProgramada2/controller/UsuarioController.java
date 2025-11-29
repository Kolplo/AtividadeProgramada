package com.atividadeProgramada.AtividadeProgramada2.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atividadeProgramada.AtividadeProgramada2.entity.Usuario;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String user(Authentication authentication, Model model) {
        Usuario user = (Usuario) authentication.getPrincipal();
        model.addAttribute("usuario", user);
        return "usuario/usuario_dados";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        Usuario user = (Usuario) authentication.getPrincipal();
        model.addAttribute("usuario", user);
        return "usuario/usuario_home_dashboard";
    }



}
