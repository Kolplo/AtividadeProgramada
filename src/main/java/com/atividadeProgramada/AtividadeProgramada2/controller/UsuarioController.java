package com.atividadeProgramada.AtividadeProgramada2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atividadeProgramada.AtividadeProgramada2.entity.Usuario;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    @GetMapping
    public String user() {
        // Redirect to the alunos listing which prepares the required model
        return "ok";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth";
        }
        Usuario user = (Usuario) authentication.getPrincipal();
        model.addAttribute("usuario", user);
        return "usuario/usuario_home_dashboard";
    }



}
